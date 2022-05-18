package com.tuum.testassignment.serviceUnitTests;

import com.tuum.testassignment.exception.AccountNotFoundException;
import com.tuum.testassignment.exception.InvalidCurrencyException;
import com.tuum.testassignment.mapper.AccountMapper;
import com.tuum.testassignment.mapper.BalanceMapper;
import com.tuum.testassignment.mapper.CurrencyMapper;
import com.tuum.testassignment.messageQueue.RabbitMQProducer;
import com.tuum.testassignment.model.domain.Account;
import com.tuum.testassignment.model.dto.AccountCreateDto;
import com.tuum.testassignment.model.dto.AccountViewDto;
import com.tuum.testassignment.service.AccountService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static com.tuum.testassignment.util.TestUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class AccountServiceTest {

    @Mock
    AccountMapper accountMapper;

    @Mock
    BalanceMapper balanceMapper;

    @Mock
    RabbitMQProducer mqProducer;

    @Mock
    CurrencyMapper currencyMapper;

    @InjectMocks
    AccountService accountService;

    @Test
    public void testGetAccountById() {
        Account account = createMockAccount();
        when(accountMapper.getAccountById(ACCOUNT_ID)).thenReturn(Optional.of(account));

        Account accountFromService = accountService.getAccountById(ACCOUNT_ID);

        assertEquals(accountFromService, account);

        verify(accountMapper, times(1)).getAccountById(ACCOUNT_ID);
    }

    @Test
    public void testGetAccountDtoById() {
        Account account = createMockAccount();

        when(accountMapper.getAccountById(ACCOUNT_ID)).thenReturn(Optional.of(account));

        AccountViewDto accountViewDto = accountService.getAccountDtoById(ACCOUNT_ID);

        assertEquals(accountViewDto.getId(), account.getId());

        verify(accountMapper, times(1)).getAccountById(ACCOUNT_ID);
    }

    @Test
    public void testGetAccountByRandomIdThrowsException() {
        UUID randomId = UUID.randomUUID();
        when(accountMapper.getAccountById(randomId)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> accountService.getAccountById(randomId));
        verify(accountMapper, times(1)).getAccountById(randomId);
    }

    @Test
    public void testCreateNewAccount() {
        Account account = createMockAccount();
        doNothing().when(accountMapper).create(account);

        when(currencyMapper.getAllCurrencies()).thenReturn(createMockCurrencies());

        AccountCreateDto dto = createMockAccountCreationDto();
        AccountViewDto createdAccount = accountService.createNewAccount(dto);

        assertEquals(createdAccount.getBalances().size(), CURRENCIES.size());
    }

    @Test
    public void testCreateNewAccountThrowsException() {
        Account account = createMockAccount();
        doNothing().when(accountMapper).create(account);

        when(currencyMapper.getAllCurrencies()).thenReturn(createMockCurrencies());

        AccountCreateDto dto = createMockAccountCreationDto();
        dto.setCurrencies(Arrays.asList("Usd", "UER"));

        assertThrows(InvalidCurrencyException.class, () -> accountService.createNewAccount(dto));
        verify(currencyMapper, times(1)).getAllCurrencies();
    }
}
