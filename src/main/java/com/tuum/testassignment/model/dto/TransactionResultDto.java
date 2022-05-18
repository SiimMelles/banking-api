package com.tuum.testassignment.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
public class TransactionResultDto {
    public TransactionResultDto(UUID accountId, BigDecimal amount, String currency, String transactionDirection, String description) {
        this.accountId = accountId;
        this.amount = amount;
        this.currency = currency;
        this.transactionDirection = transactionDirection;
        this.description = description;
    }

    private UUID accountId;
    private UUID id;
    private BigDecimal amount;
    private String currency;
    private String transactionDirection;
    private String description;
    private BigDecimal remainingBalance;
}
