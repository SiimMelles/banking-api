package com.tuum.testassignment.service;

import com.tuum.testassignment.exception.AccountNotFoundException;
import com.tuum.testassignment.exception.InvalidCurrencyException;
import com.tuum.testassignment.mapper.AccountMapper;
import com.tuum.testassignment.mapper.BalanceMapper;
import com.tuum.testassignment.mapper.CurrencyMapper;
import com.tuum.testassignment.messageQueue.RabbitMQProducer;
import com.tuum.testassignment.model.domain.Account;
import com.tuum.testassignment.model.domain.Balance;
import com.tuum.testassignment.model.domain.Currency;
import com.tuum.testassignment.model.dto.AccountCreateDto;
import com.tuum.testassignment.model.dto.AccountViewDto;
import com.tuum.testassignment.serviceMapper.AccountObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.tuum.testassignment.serviceMapper.AccountObjectMapper.mapCreateDtoToDomainObject;

@Service
public class AccountService {
    private final AccountMapper accountMapper;

    private final BalanceMapper balanceMapper;

    private final CurrencyMapper currencyMapper;

    private final RabbitMQProducer mqSender;

    public AccountService(AccountMapper accountMapper, BalanceMapper balanceMapper, CurrencyMapper currencyMapper, RabbitMQProducer mqSender) {
        this.accountMapper = accountMapper;
        this.balanceMapper = balanceMapper;
        this.currencyMapper = currencyMapper;
        this.mqSender = mqSender;
    }

    public AccountViewDto getAccountDtoById(UUID accountId) throws AccountNotFoundException {
        return AccountObjectMapper.mapDomainObjectToDto(getAccountById(accountId));
    }

    public Account getAccountById(UUID accountId) throws AccountNotFoundException {
        Optional<Account> account = accountMapper.getAccountById(accountId);
        if (account.isEmpty()) {
            throw new AccountNotFoundException(accountId);
        }
        return account.get();
    }

    @Transactional(rollbackFor = Exception.class)
    public AccountViewDto createNewAccount(AccountCreateDto creationDto) throws InvalidCurrencyException {
        Account newAccount = mapCreateDtoToDomainObject(creationDto);
        accountMapper.create(newAccount);
        List<Currency> currencies = currencyMapper.getAllCurrencies();
        creationDto.getCurrencies().replaceAll(String::toUpperCase);
        Set<String> currencySet = new HashSet<>(creationDto.getCurrencies());
        for (String currency : currencySet) {
            Balance b = currencies.stream()
                    .filter(x -> x.getSymbol().equals(currency))
                    .findFirst()
                    .map(x -> {
                        Balance balance = new Balance(newAccount.getId(), x.getId());
                        balanceMapper.create(balance);
                        mqSender.send("Created balance (id : " + balance.getId() + ") with currency '" + x.getSymbol()
                                + "' for account (id: " + balance.getAccountId() + ")"); // Maybe collect into list and send at the end of transaction?
                                balance.setCurrency(x);
                        return balance;
            }).orElseThrow(() -> new InvalidCurrencyException(currency));
            newAccount.addBalance(b);
        }
        mqSender.send("Created account (id : " + newAccount.getId() + ") with balances in currencies: " +
                String.join(", ", creationDto.getCurrencies()));
        return AccountObjectMapper.mapDomainObjectToDto(newAccount);
    }

}
