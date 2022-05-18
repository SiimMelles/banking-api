package com.tuum.testassignment.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
public class BalanceViewDto {
    private UUID id;
    private BigDecimal amount;
    private String currency;
}
