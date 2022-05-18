package com.tuum.testassignment.controller;

import com.tuum.testassignment.model.dto.TransactionCreateDto;
import com.tuum.testassignment.model.dto.TransactionResultDto;
import com.tuum.testassignment.model.dto.TransactionViewDto;
import com.tuum.testassignment.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/transaction")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping("/{accountId}")
    public List<TransactionViewDto> getAllTransactionsForAccount(@PathVariable UUID accountId) {
        return transactionService.getAllByAccountId(accountId);
    }

    @PostMapping
    public TransactionResultDto createTransaction(@RequestBody TransactionCreateDto transactionCreateDto) {
        return transactionService.createTransaction(transactionCreateDto);
    }
}
