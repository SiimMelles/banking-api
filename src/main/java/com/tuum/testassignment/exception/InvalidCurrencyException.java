package com.tuum.testassignment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class InvalidCurrencyException extends ResponseStatusException {

    public InvalidCurrencyException(String currency) {
        super(HttpStatus.BAD_REQUEST, "Currency is not supported by the API. Currency: " + currency);
    }
}
