package com.tuum.testassignment.mapper;

import com.tuum.testassignment.model.domain.Currency;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.UUID;

@Mapper
public interface CurrencyMapper {

    @Select("SELECT * FROM currency")
    List<Currency> getAllCurrencies();

    @Select("SELECT * FROM currency WHERE id = #{currencyId}")
    Currency getCurrencyById(UUID currencyId);
}
