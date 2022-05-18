package com.tuum.testassignment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;

public class InvalidTransactionAmountException  extends ResponseStatusException {
    public InvalidTransactionAmountException(BigDecimal amount) {
        super(HttpStatus.BAD_REQUEST, "Transaction amount is invalid: " + amount);
    }
}
