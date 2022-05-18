package com.tuum.testassignment.model.domain;

public enum TransactionDirection {
    IN("in"), OUT("out");

    private final String direction;

    TransactionDirection(String direction) {
        this.direction = direction;
    }

    @Override
    public String toString() {
        return direction;
    }
}
