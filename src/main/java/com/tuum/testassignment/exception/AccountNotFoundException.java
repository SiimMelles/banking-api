package com.tuum.testassignment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

public class AccountNotFoundException extends ResponseStatusException {

    public AccountNotFoundException(UUID accountId) {
        super(HttpStatus.BAD_REQUEST, "Account not found for id: " + accountId);
    }
}
