package com.tuum.testassignment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

public class MissingBalanceException extends ResponseStatusException {
    public MissingBalanceException(String currency, UUID accountId) {
        super(HttpStatus.BAD_REQUEST, "Balance for currency '" + currency + "' does not exist on account with id: " + accountId);
    }
}
