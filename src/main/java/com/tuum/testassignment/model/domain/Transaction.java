package com.tuum.testassignment.model.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
public class Transaction {
    private UUID id;
    private UUID accountId;
    private BigDecimal amount;

    private UUID currencyId;
    private Currency currency;

    private String transactionDirection;
    private String description;

    public Transaction(UUID accountId, BigDecimal amount, UUID currencyId, String transactionDirection, String description) {
        this.accountId = accountId;
        this.amount = amount;
        this.currencyId = currencyId;
        this.transactionDirection = transactionDirection;
        this.description = description;
    }

    public Transaction(UUID id, UUID accountId, BigDecimal amount, UUID currencyId, String transactionDirection, String description) {
        this(accountId, amount, currencyId, transactionDirection, description);
        this.id = id;
    }
}
