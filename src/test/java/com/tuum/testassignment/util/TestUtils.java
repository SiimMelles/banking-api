package com.tuum.testassignment.util;

import com.tuum.testassignment.model.domain.Account;
import com.tuum.testassignment.model.domain.Balance;
import com.tuum.testassignment.model.domain.Currency;
import com.tuum.testassignment.model.domain.Transaction;
import com.tuum.testassignment.model.dto.AccountCreateDto;
import com.tuum.testassignment.model.dto.TransactionCreateDto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.tuum.testassignment.model.domain.TransactionDirection.IN;

public class TestUtils {
    public static final UUID ACCOUNT_ID = UUID.fromString("7f6c87bd-543e-468d-9db5-1c92641eec8d");
    public static final UUID CUSTOMER_ID = UUID.fromString("47c44875-ad94-4861-b60d-8bade8937083");
    public static final UUID TRANSACTION_ID = UUID.fromString("e59a4478-5e4e-460f-ab51-bb308426b01e");
    public static final UUID CURRENCY_ID = UUID.fromString("e59a4478-5e4e-460f-ab51-bb308426b01e");
    public static final UUID BALANCE_ID = UUID.fromString("161ead6b-9466-4df1-a44e-0ee7a0afa01d");

    public static final String COUNTRY = "Estonia";
    public static final String TRANSACTION_DESCRIPTION = "Payment for service";
    public static final List<String> CURRENCIES = Arrays.asList("USD", "EUR");

    public static Account createMockAccount() {
        return createMockAccountWithId(ACCOUNT_ID);
    }

    public static Account createMockAccountWithId(UUID id) {
        return new Account(id, CUSTOMER_ID, COUNTRY, new ArrayList<>());
    }

    public static AccountCreateDto createMockAccountCreationDto() {
        return new AccountCreateDto(CUSTOMER_ID, COUNTRY, CURRENCIES);
    }

    public static List<Currency> createMockCurrencies() {
        return CURRENCIES.stream().map(Currency::new).collect(Collectors.toList());
    }

    public static Currency createMockCurrency() {
        return new Currency(CURRENCY_ID, CURRENCIES.get(0));
    }

    public static Transaction createMockTransaction(UUID currencyId) {
        return new Transaction(TRANSACTION_ID, ACCOUNT_ID, BigDecimal.valueOf(25L), currencyId, IN.toString(), TRANSACTION_DESCRIPTION);
    }

    public static TransactionCreateDto createMockTransactionCreationDto() {
        return new TransactionCreateDto(ACCOUNT_ID, BigDecimal.valueOf(25L), CURRENCIES.get(0), IN.toString(), TRANSACTION_DESCRIPTION);
    }

    public static Balance createMockBalance() {
        Balance balance = new Balance(BALANCE_ID, ACCOUNT_ID, CURRENCY_ID);
        balance.setCurrency(createMockCurrency());
        return balance;
    }
}
