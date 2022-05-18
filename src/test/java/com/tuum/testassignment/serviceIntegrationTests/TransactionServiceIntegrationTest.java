package com.tuum.testassignment.serviceIntegrationTests;

import com.tuum.testassignment.model.dto.*;
import com.tuum.testassignment.service.AccountService;
import com.tuum.testassignment.service.TransactionService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static com.tuum.testassignment.model.domain.TransactionDirection.OUT;
import static com.tuum.testassignment.util.TestUtils.createMockAccountCreationDto;
import static com.tuum.testassignment.util.TestUtils.createMockTransactionCreationDto;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class TransactionServiceIntegrationTest extends IntegrationTestBase {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private AccountService accountService;

    @DynamicPropertySource
    static void setRabbitMQPort(DynamicPropertyRegistry registry) {
        registry.add("spring.rabbitmq.port", () -> rabbitMQContainer.getMappedPort(5672));
    }

    @Test
    public void testCreateInTransaction() {
        AccountCreateDto dto = createMockAccountCreationDto();
        AccountViewDto createdAccount = accountService.createNewAccount(dto);

        TransactionCreateDto transactionCreateDto = createMockTransactionCreationDto();
        transactionCreateDto.setAccountId(createdAccount.getId());
        TransactionResultDto resultDto = transactionService.createTransaction(transactionCreateDto);

        assertEquals(createdAccount.getId(), resultDto.getAccountId());

        createdAccount = accountService.getAccountDtoById(createdAccount.getId());
        BalanceViewDto balance = createdAccount.getBalances()
                .stream()
                .filter(x -> x.getCurrency().equals(transactionCreateDto.getCurrency()))
                .findFirst().get();

        assertEquals(balance.getAmount().compareTo(transactionCreateDto.getAmount()), 0);
    }

    @Test
    public void testCreateInTransactionToMissingBalance() {
        AccountCreateDto dto = createMockAccountCreationDto();
        dto.setCurrencies(Arrays.asList("USD"));
        AccountViewDto createdAccount = accountService.createNewAccount(dto);

        TransactionCreateDto transactionCreateDto = createMockTransactionCreationDto();

        assertEquals(createdAccount.getBalances().size(), 1);
        assertEquals(createdAccount.getBalances().stream().findFirst().get().getCurrency(), "USD");

        transactionCreateDto.setCurrency("EUR");
        transactionCreateDto.setAccountId(createdAccount.getId());
        TransactionResultDto resultDto = transactionService.createTransaction(transactionCreateDto);

        assertEquals(createdAccount.getId(), resultDto.getAccountId());

        createdAccount = accountService.getAccountDtoById(createdAccount.getId());
        BalanceViewDto balance = createdAccount.getBalances()
                .stream()
                .filter(x -> x.getCurrency().equals(transactionCreateDto.getCurrency()))
                .findFirst().get();

        assertEquals(balance.getAmount().compareTo(transactionCreateDto.getAmount()), 0);
        assertEquals(createdAccount.getBalances().size(), 2);
    }

    @Test
    public void testCreateOutTransaction() {
        AccountCreateDto dto = createMockAccountCreationDto();
        AccountViewDto createdAccount = accountService.createNewAccount(dto);

        TransactionCreateDto transactionCreateDto = createMockTransactionCreationDto();
        transactionCreateDto.setAccountId(createdAccount.getId());
        TransactionResultDto resultDto = transactionService.createTransaction(transactionCreateDto);

        assertEquals(createdAccount.getId(), resultDto.getAccountId());

        createdAccount = accountService.getAccountDtoById(createdAccount.getId());
        BalanceViewDto balance = createdAccount.getBalances()
                .stream()
                .filter(x -> x.getCurrency().equals(transactionCreateDto.getCurrency()))
                .findFirst().get();

        assertEquals(balance.getAmount().compareTo(transactionCreateDto.getAmount()), 0);

        transactionCreateDto.setTransactionDirection(OUT.toString());
        transactionCreateDto.setAmount(BigDecimal.valueOf(10));
        transactionService.createTransaction(transactionCreateDto);

        createdAccount = accountService.getAccountDtoById(createdAccount.getId());
        balance = createdAccount.getBalances()
                .stream()
                .filter(x -> x.getCurrency().equals(transactionCreateDto.getCurrency()))
                .findFirst().get();

        assertEquals(balance.getAmount().compareTo(BigDecimal.valueOf(15)), 0);
    }

    @Test
    public void testGetTransactionsByAccountId() {
        AccountCreateDto dto = createMockAccountCreationDto();
        AccountViewDto createdAccount = accountService.createNewAccount(dto);

        List<TransactionViewDto> transactionViewDto = transactionService.getAllByAccountId(createdAccount.getId());
        assertEquals(transactionViewDto.size(), 0);

        TransactionCreateDto transactionCreateDto = createMockTransactionCreationDto();
        transactionCreateDto.setAccountId(createdAccount.getId());
        TransactionResultDto resultDto = transactionService.createTransaction(transactionCreateDto);

        assertEquals(createdAccount.getId(), resultDto.getAccountId());

        transactionViewDto = transactionService.getAllByAccountId(createdAccount.getId());

        assertEquals(transactionViewDto.size(), 1);
    }
}
