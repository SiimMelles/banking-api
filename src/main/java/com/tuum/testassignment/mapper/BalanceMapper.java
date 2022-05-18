package com.tuum.testassignment.mapper;

import com.tuum.testassignment.model.domain.Balance;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.UUID;


@Mapper
public interface BalanceMapper {

    @Options(useGeneratedKeys=true, keyProperty="id", keyColumn="id")
    @Insert("INSERT INTO balance (account_id, currency_id, amount) VALUES (#{accountId}, #{currencyId}, #{amount})")
    void create(Balance balance);

    @Results({
            @Result(property = "id", column = "id", id = true),
            @Result(property = "amount", column = "amount"),
            @Result(property = "currency", column = "currency_id",
                    one = @One(select = "com.tuum.testassignment.mapper.CurrencyMapper.getCurrencyById")),
    })
    @Select("SELECT id, currency_id, amount FROM balance WHERE account_id = #{accountId}")
    List<Balance> getAllForAccount(UUID accountId);

    @Update("UPDATE balance SET amount = #{amount} WHERE id = #{id}")
    void updateBalance(Balance balance);

}
