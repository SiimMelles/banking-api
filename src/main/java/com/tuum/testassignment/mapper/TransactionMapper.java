package com.tuum.testassignment.mapper;

import com.tuum.testassignment.model.domain.Transaction;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.UUID;

@Mapper
public interface TransactionMapper {

    @Options(useGeneratedKeys=true, keyProperty="id", keyColumn="id")
    @Insert("INSERT INTO transaction (account_id, amount, currency_id, direction, description) " +
            "VALUES (#{accountId}, #{amount}, #{currencyId}, #{transactionDirection}, #{description})")
    void createTransaction(Transaction transaction);

    @Results({
            @Result(property = "id", column = "id", id = true),
            @Result(property = "accountId", column = "account_id"),
            @Result(property = "amount", column = "amount"),
            @Result(property = "currency", column = "currency_id",
                    one = @One(select = "com.tuum.testassignment.mapper.CurrencyMapper.getCurrencyById")),
            @Result(property = "transactionDirection", column = "direction"),
            @Result(property = "description", column = "description"),
    })
    @Select("SELECT * FROM transaction WHERE transaction.account_id = #{accountId}")
    List<Transaction> getAllTransactionsByAccountId(UUID accountId);
}
