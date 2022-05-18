package com.tuum.testassignment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class InsufficientFundsException  extends ResponseStatusException {
    public InsufficientFundsException(String currency) {
        super(HttpStatus.BAD_REQUEST, "Account balance does not have enough funds in currency: " + currency);
    }
}
