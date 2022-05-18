package com.tuum.testassignment.model.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class Currency {
    private UUID id;
    private String symbol;

    public Currency(String symbol) {
        this.symbol = symbol;
    }
}
