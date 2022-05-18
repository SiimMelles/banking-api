package com.tuum.testassignment.serviceMapper;

import com.tuum.testassignment.model.domain.Account;
import com.tuum.testassignment.model.dto.AccountCreateDto;
import com.tuum.testassignment.model.dto.AccountViewDto;
import com.tuum.testassignment.model.dto.BalanceViewDto;

import java.util.stream.Collectors;

public class AccountObjectMapper {

    private AccountObjectMapper() {
    }

    public static AccountViewDto mapDomainObjectToDto(Account account) {
        return new AccountViewDto(account.getId(),
                account.getCustomerId(),
                account.getCountry(),
                account.getBalances().stream()
                        .map(balance -> new BalanceViewDto(balance.getId(), balance.getAmount(), balance.getCurrency().getSymbol()))
                        .collect(Collectors.toList()));
    }

    public static Account mapCreateDtoToDomainObject(AccountCreateDto dto) {
        return new Account(dto.getCustomerId(), dto.getCountry());
    }
}
