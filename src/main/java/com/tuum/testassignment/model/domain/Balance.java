package com.tuum.testassignment.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.UUID;

@Data
public class Balance {
    private UUID id;

    private UUID accountId;
    @JsonIgnore
    private Account account;

    private UUID currencyId;
    private Currency currency;

    private BigDecimal amount;

    public Balance(UUID id, UUID accountId, UUID currencyId) {
        this(accountId, currencyId);
        this.id = id;
    }

    public Balance(UUID accountId, UUID currencyId) {
        this(accountId, currencyId, new BigDecimal(BigInteger.ZERO));
    }
    public Balance(UUID accountId, UUID currencyId, BigDecimal amount) {
        this.accountId = accountId;
        this.currencyId = currencyId;
        this.amount = amount;
    }

    public void doTransaction(BigDecimal transactionAmount) {
        amount = amount.add(transactionAmount);
    }
}
