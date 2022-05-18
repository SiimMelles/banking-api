package com.tuum.testassignment.serviceUnitTests;

import com.tuum.testassignment.exception.*;
import com.tuum.testassignment.mapper.BalanceMapper;
import com.tuum.testassignment.mapper.CurrencyMapper;
import com.tuum.testassignment.mapper.TransactionMapper;
import com.tuum.testassignment.model.domain.Account;
import com.tuum.testassignment.model.domain.Balance;
import com.tuum.testassignment.model.domain.Currency;
import com.tuum.testassignment.model.domain.Transaction;
import com.tuum.testassignment.model.dto.TransactionCreateDto;
import com.tuum.testassignment.service.AccountService;
import com.tuum.testassignment.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.List;

import static com.tuum.testassignment.model.domain.TransactionDirection.OUT;
import static com.tuum.testassignment.util.TestUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class TransactionServiceTest {

    @Mock
    CurrencyMapper currencyMapper;

    @Mock
    BalanceMapper balanceMapper;

    @Mock
    TransactionMapper transactionMapper;

    @Mock
    AccountService accountService;

    @InjectMocks
    TransactionService transactionService;

    @Test
    public void testGetTransactionsByAccountId() {
        when(accountService.getAccountById(ACCOUNT_ID)).thenReturn(createMockAccount());
        Transaction transaction = createMockTransaction(CURRENCY_ID);
        transaction.setCurrency(createMockCurrency());
        when(transactionMapper.getAllTransactionsByAccountId(ACCOUNT_ID)).thenReturn(List.of(transaction));

        transactionService.getAllByAccountId(ACCOUNT_ID);
        verify(transactionMapper, times(1)).getAllTransactionsByAccountId(ACCOUNT_ID);
    }

    @Test
    public void testCreateTransactionWithInvalidDescriptionThrowsException() {
        TransactionCreateDto dto = createMockTransactionCreationDto();
        dto.setDescription("  ");
        assertThrows(MissingDescriptionException.class, () -> transactionService.createTransaction(dto));
    }

    @Test
    public void testCreateTransactionWithNullDescriptionThrowsException() {
        TransactionCreateDto dto = createMockTransactionCreationDto();
        dto.setDescription(null);
        assertThrows(MissingDescriptionException.class, () -> transactionService.createTransaction(dto));
    }

    @Test
    public void testCreateTransactionWithInvalidAmountThrowsException() {
        TransactionCreateDto dto = createMockTransactionCreationDto();
        dto.setAmount(BigDecimal.valueOf(-100L));
        assertThrows(InvalidTransactionAmountException.class, () -> transactionService.createTransaction(dto));
    }

    @Test
    public void testCreateTransactionWithAmountNullThrowsException() {
        TransactionCreateDto dto = createMockTransactionCreationDto();
        dto.setAmount(null);
        assertThrows(InvalidTransactionAmountException.class, () -> transactionService.createTransaction(dto));
    }

    @Test
    public void testCreateTransactionWithInvalidDirectionThrowsException() {
        TransactionCreateDto dto = createMockTransactionCreationDto();
        dto.setTransactionDirection("Wrong direction");
        assertThrows(InvalidTransactionDirectionException.class, () -> transactionService.createTransaction(dto));
    }

    @Test
    public void testCreateTransactionWithDirectionNullThrowsException() {
        TransactionCreateDto dto = createMockTransactionCreationDto();
        dto.setTransactionDirection(null);
        assertThrows(InvalidTransactionDirectionException.class, () -> transactionService.createTransaction(dto));
    }

    @Test
    public void testCreateTransactionWithInvalidCurrencyThrowsException() {
        Currency currency = createMockCurrency();
        TransactionCreateDto dto = createMockTransactionCreationDto();
        dto.setCurrency("Wrong Currency");
        when(currencyMapper.getAllCurrencies()).thenReturn(List.of(currency));
        assertThrows(InvalidCurrencyException.class, () -> transactionService.createTransaction(dto));
    }

    @Test
    public void testCreateTransactionWithInvalidAccountIdThrowsException() {
        Currency currency = createMockCurrency();
        TransactionCreateDto dto = createMockTransactionCreationDto();

        when(currencyMapper.getAllCurrencies()).thenReturn(List.of(currency));
        when(accountService.getAccountById(ACCOUNT_ID)).thenThrow(new AccountNotFoundException(ACCOUNT_ID));

        assertThrows(AccountNotFoundException.class, () -> transactionService.createTransaction(dto));
    }

    @Test
    public void testInsufficientFundsOnBalanceThrowsException() {
        Currency currency = createMockCurrency();
        TransactionCreateDto dto = createMockTransactionCreationDto();
        Balance balance = createMockBalance();
        Account account = createMockAccount();

        dto.setTransactionDirection(OUT.toString());
        account.addBalance(balance);

        when(currencyMapper.getAllCurrencies()).thenReturn(List.of(currency));
        when(accountService.getAccountById(ACCOUNT_ID)).thenReturn(account);

        assertThrows(InsufficientFundsException.class, () -> transactionService.createTransaction(dto));
    }

    @Test
    public void testTransactionOutSuccessful() {
        BigDecimal balanceAmount = BigDecimal.valueOf(1000L);
        Currency currency = createMockCurrency();
        TransactionCreateDto dto = createMockTransactionCreationDto();
        Balance balance = createMockBalance();
        balance.setAmount(balanceAmount);
        Account account = createMockAccount();

        dto.setTransactionDirection(OUT.toString());
        account.addBalance(balance);

        when(currencyMapper.getAllCurrencies()).thenReturn(List.of(currency));
        when(accountService.getAccountById(ACCOUNT_ID)).thenReturn(account);

        transactionService.createTransaction(dto);
        assertEquals(balance.getAmount(), balanceAmount.subtract(dto.getAmount()));
    }

    @Test
    public void testTransactionInSuccessful() {
        BigDecimal balanceAmount = BigDecimal.valueOf(1000L);
        Currency currency = createMockCurrency();
        TransactionCreateDto dto = createMockTransactionCreationDto();
        Balance balance = createMockBalance();
        balance.setAmount(balanceAmount);
        Account account = createMockAccount();

        account.addBalance(balance);

        when(currencyMapper.getAllCurrencies()).thenReturn(List.of(currency));
        when(accountService.getAccountById(ACCOUNT_ID)).thenReturn(account);

        transactionService.createTransaction(dto);
        assertEquals(balance.getAmount(), balanceAmount.add(dto.getAmount()));
    }

    @Test
    public void testTransactionOutWithMissingBalanceThrowsException() {
        Currency currency = createMockCurrency();
        TransactionCreateDto dto = createMockTransactionCreationDto();
        dto.setTransactionDirection(OUT.toString());

        when(currencyMapper.getAllCurrencies()).thenReturn(List.of(currency));
        when(accountService.getAccountById(ACCOUNT_ID)).thenReturn(createMockAccount());

        assertThrows(MissingBalanceException.class, () -> transactionService.createTransaction(dto));
    }

    @Test
    public void testTransactionInWithMissingBalanceCreatesBalance() {
        Currency currency = createMockCurrency();
        TransactionCreateDto dto = createMockTransactionCreationDto();

        when(currencyMapper.getAllCurrencies()).thenReturn(List.of(currency));
        when(accountService.getAccountById(ACCOUNT_ID)).thenReturn(createMockAccount());

        transactionService.createTransaction(dto);

        verify(balanceMapper, times(1))
                .create(new Balance(dto.getAccountId(),currency.getId(), dto.getAmount()));
    }
}
