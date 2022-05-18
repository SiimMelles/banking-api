package com.tuum.testassignment.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
public class TransactionViewDto {
    private UUID id;
    private UUID accountId;
    private BigDecimal amount;
    private String currency;
    private String transactionDirection;
    private String description;
}
