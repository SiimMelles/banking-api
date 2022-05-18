package com.tuum.testassignment.serviceMapper;

import com.tuum.testassignment.model.domain.Transaction;
import com.tuum.testassignment.model.dto.TransactionCreateDto;
import com.tuum.testassignment.model.dto.TransactionResultDto;
import com.tuum.testassignment.model.dto.TransactionViewDto;

import java.util.UUID;

public class TransactionObjectMapper {

    private TransactionObjectMapper() {
    }

    public static Transaction mapTransactionCreateDtoToDomainObject(TransactionCreateDto dto, UUID currencyId) {
        return new Transaction(dto.getAccountId(), dto.getAmount(), currencyId,
                dto.getTransactionDirection(), dto.getDescription());
    }

    public static TransactionResultDto mapTransactionCreateDtoToResultDto(TransactionCreateDto createDto) {
        return new TransactionResultDto(createDto.getAccountId(), createDto.getAmount(), createDto.getCurrency(),
                createDto.getTransactionDirection(), createDto.getDescription());
    }

    public static TransactionViewDto mapTransactionDomainObjectToViewDto(Transaction domainObject) {
        return new TransactionViewDto(domainObject.getId(), domainObject.getAccountId(), domainObject.getAmount(),
                domainObject.getCurrency().getSymbol(), domainObject.getTransactionDirection(), domainObject.getDescription());
    }
}
