package com.tuum.testassignment.model.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class Account {

    private UUID id;
    private UUID customerId;
    private String country;
    private List<Balance> balances;

    public void addBalance(Balance balance) {
        balances.add(balance);
    }

    public Account(UUID id, UUID customerId, String country) {
        this.id = id;
        this.customerId = customerId;
        this.country = country;
    }

    public Account(UUID customerId, String country) {
        this.customerId = customerId;
        this.country = country;
        balances = new ArrayList<>();
    }
}
