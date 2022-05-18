package com.tuum.testassignment.service;

import com.tuum.testassignment.exception.*;
import com.tuum.testassignment.mapper.BalanceMapper;
import com.tuum.testassignment.mapper.CurrencyMapper;
import com.tuum.testassignment.mapper.TransactionMapper;
import com.tuum.testassignment.messageQueue.RabbitMQProducer;
import com.tuum.testassignment.model.domain.*;
import com.tuum.testassignment.model.dto.TransactionCreateDto;
import com.tuum.testassignment.model.dto.TransactionResultDto;
import com.tuum.testassignment.model.dto.TransactionViewDto;
import com.tuum.testassignment.serviceMapper.TransactionObjectMapper;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.tuum.testassignment.model.domain.TransactionDirection.OUT;
import static com.tuum.testassignment.serviceMapper.TransactionObjectMapper.*;

@Service
public class TransactionService {

    private final CurrencyMapper currencyMapper;

    private final BalanceMapper balanceMapper;

    private final TransactionMapper transactionMapper;

    private final AccountService accountService;

    private final RabbitMQProducer mqSender;

    public TransactionService(CurrencyMapper currencyMapper, BalanceMapper balanceMapper, TransactionMapper transactionMapper, AccountService accountService, RabbitMQProducer mqProducer) {
        this.currencyMapper = currencyMapper;
        this.balanceMapper = balanceMapper;
        this.transactionMapper = transactionMapper;
        this.accountService = accountService;
        this.mqSender = mqProducer;
    }

    public List<TransactionViewDto> getAllByAccountId(UUID accountId) {
        accountService.getAccountById(accountId);
        List <Transaction> transactions = transactionMapper.getAllTransactionsByAccountId(accountId);
        return transactions.stream().map(TransactionObjectMapper::mapTransactionDomainObjectToViewDto).collect(Collectors.toList());

    }

    @Transactional(rollbackFor = Exception.class)
    public TransactionResultDto createTransaction(TransactionCreateDto createDto) {
        // Invalid Description
        if (createDto.getDescription() == null || createDto.getDescription().isBlank()) throw new MissingDescriptionException();

        // Invalid Amount
        if (createDto.getAmount() == null || createDto.getAmount().compareTo(BigDecimal.ZERO) < 0) throw new InvalidTransactionAmountException(createDto.getAmount());

        // Invalid Direction
        if (!EnumUtils.isValidEnumIgnoreCase(TransactionDirection.class, createDto.getTransactionDirection()))
            throw new InvalidTransactionDirectionException(createDto.getTransactionDirection());

        // Invalid Currency
        List<Currency> currencies = currencyMapper.getAllCurrencies();
        Currency transactionCurrency = currencies.stream()
                .filter(c -> c.getSymbol().equals(createDto.getCurrency()))
                .findFirst()
                .orElseThrow(() -> new InvalidCurrencyException(createDto.getCurrency()));

        // Account missing
        Account account = accountService.getAccountById(createDto.getAccountId());

        Optional<Balance> transactionBalance = account.getBalances().stream()
                .filter(balance -> balance.getCurrency().getSymbol().equals(createDto.getCurrency())).findFirst();

        TransactionResultDto transactionResult = mapTransactionCreateDtoToResultDto(createDto);


        if (createDto.getTransactionDirection().equals(OUT.toString())) {
            transactionBalance.ifPresentOrElse(balance -> {
                if (createDto.getAmount().compareTo(balance.getAmount()) > 0) {
                    throw new InsufficientFundsException(createDto.getCurrency());
                }
                balance.doTransaction(createDto.getAmount().negate());
                balanceMapper.updateBalance(balance);
                mqSender.send("Updated '" + transactionCurrency.getSymbol() +  "' balance of account (id: " + account.getId()
                        + "). Transaction direction: " + createDto.getTransactionDirection() + ", amount: " + createDto.getAmount());
                transactionResult.setRemainingBalance(balance.getAmount());
            }, () -> {
                throw new MissingBalanceException(createDto.getCurrency(), createDto.getAccountId());
            });
        } else {
            transactionBalance.ifPresentOrElse(balance -> {
                balance.doTransaction(createDto.getAmount());
                balanceMapper.updateBalance(balance);
                mqSender.send("Updated '" + transactionCurrency.getSymbol() +  "' balance of account (id: " + account.getId()
                        + "). Transaction direction: " + createDto.getTransactionDirection() + ", amount: " + createDto.getAmount());
                transactionResult.setRemainingBalance(balance.getAmount());
            }, () -> {
                Balance balance = new Balance(createDto.getAccountId(), transactionCurrency.getId(), createDto.getAmount());
                balanceMapper.create(balance);
                mqSender.send("Created balance (id : " + balance.getId() + ") with currency '" + transactionCurrency.getSymbol()
                        + "' for account (id: " + balance.getAccountId() + ")");
                transactionResult.setRemainingBalance(createDto.getAmount());
            });
        }
        Transaction transaction = mapTransactionCreateDtoToDomainObject(createDto, transactionCurrency.getId());
        transactionMapper.createTransaction(transaction);
        mqSender.send("Created transaction for account: " + createDto.getAccountId() + ", direction: " +
                createDto.getTransactionDirection() + ", amount: " + createDto.getAmount() + ", currency: " + createDto.getCurrency());

        transactionResult.setId(transaction.getId());
        return transactionResult;
    }
}
