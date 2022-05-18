package com.tuum.testassignment.mapper;

import com.tuum.testassignment.model.domain.Account;
import org.apache.ibatis.annotations.*;

import java.util.Optional;
import java.util.UUID;

@Mapper
public interface AccountMapper {

    @Results({
            @Result(property = "id", column = "id", id = true),
            @Result(property = "customerId", column = "customer_id"),
            @Result(property = "country", column = "country"),
            @Result(property = "balances", column = "id",
                    many = @Many(select = "com.tuum.testassignment.mapper.BalanceMapper.getAllForAccount"))
    })
    @Select("SELECT * FROM account where account.id = #{accountId}")
    Optional<Account> getAccountById(UUID accountId);

    @Options(useGeneratedKeys=true, keyProperty="id", keyColumn="id")
    @Insert("INSERT INTO account (customer_id, country) VALUES (#{customerId}, #{country})")
    void create(Account account);

}
