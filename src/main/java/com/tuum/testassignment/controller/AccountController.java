package com.tuum.testassignment.controller;

import com.tuum.testassignment.exception.AccountNotFoundException;
import com.tuum.testassignment.exception.InvalidCurrencyException;
import com.tuum.testassignment.model.dto.AccountCreateDto;
import com.tuum.testassignment.model.dto.AccountViewDto;
import com.tuum.testassignment.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @GetMapping("/{accountId}")
    public AccountViewDto getAccountById(@PathVariable UUID accountId) throws AccountNotFoundException {
        return accountService.getAccountDtoById(accountId);
    }

    @PostMapping
    public AccountViewDto createNewAccount(@RequestBody AccountCreateDto accountCreateDto) throws InvalidCurrencyException {
        return accountService.createNewAccount(accountCreateDto);
    }
}
