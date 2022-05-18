package com.tuum.testassignment.controller;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler
    protected ResponseEntity<Object> handleConflict(ResponseStatusException ex, WebRequest request, HttpServletResponse response) {
        response.setHeader("error", ex.getMessage());
        response.setStatus(ex.getRawStatusCode());
        Map<String, Object> error = new HashMap<>();
        error.put("timestamp", Timestamp.from(Instant.now()));
        error.put("error_message", ex.getReason());
        error.put("status", ex.getRawStatusCode());
        response.setContentType(APPLICATION_JSON_VALUE);
        return handleExceptionInternal(ex, error, new HttpHeaders(), ex.getStatus(), request);
    }
}