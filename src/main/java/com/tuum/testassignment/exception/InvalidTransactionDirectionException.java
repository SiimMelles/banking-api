package com.tuum.testassignment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class InvalidTransactionDirectionException extends ResponseStatusException {
    public InvalidTransactionDirectionException(String direction) {
        super(HttpStatus.BAD_REQUEST, "Transaction direction is invalid: " + direction);
    }
}
