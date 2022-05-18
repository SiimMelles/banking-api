package com.tuum.testassignment.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class AccountViewDto {
    private UUID id;
    private UUID customerId;
    private String country;
    private List<BalanceViewDto> balances;
}
