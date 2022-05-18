package com.tuum.testassignment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class MissingDescriptionException extends ResponseStatusException {
    public MissingDescriptionException() {
        super(HttpStatus.BAD_REQUEST, "Description is missing for transaction.");
    }
}
