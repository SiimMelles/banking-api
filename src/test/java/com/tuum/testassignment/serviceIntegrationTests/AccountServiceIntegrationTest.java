package com.tuum.testassignment.serviceIntegrationTests;

import com.tuum.testassignment.model.dto.AccountCreateDto;
import com.tuum.testassignment.model.dto.AccountViewDto;
import com.tuum.testassignment.service.AccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import static com.tuum.testassignment.util.TestUtils.createMockAccountCreationDto;
import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
public class AccountServiceIntegrationTest extends IntegrationTestBase {

    @Autowired
    private AccountService accountService;

    @DynamicPropertySource
    static void setRabbitMQPort(DynamicPropertyRegistry registry) {
        registry.add("spring.rabbitmq.port", () -> rabbitMQContainer.getMappedPort(5672));
    }

    @Test
    void testCreateAccount() {
        System.out.println(rabbitMQContainer.getAmqpPort());
        AccountCreateDto dto = createMockAccountCreationDto();

        AccountViewDto createdAccount = accountService.createNewAccount(dto);

        assertEquals(createdAccount.getCustomerId(), dto.getCustomerId());
        assertEquals(createdAccount.getBalances().size(), 2);
    }

    @Test
    void testGetAccountById() {
        System.out.println(rabbitMQContainer.getAmqpPort());

        AccountCreateDto dto = createMockAccountCreationDto();
        AccountViewDto createdAccount = accountService.createNewAccount(dto);

        AccountViewDto accountViewDto = accountService.getAccountDtoById(createdAccount.getId());

        assertEquals(accountViewDto.getId(), createdAccount.getId());
        assertEquals(accountViewDto.getBalances().size(), createdAccount.getBalances().size());
    }
}
