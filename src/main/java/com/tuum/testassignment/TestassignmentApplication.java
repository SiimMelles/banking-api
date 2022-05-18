package com.tuum.testassignment;

import com.tuum.testassignment.model.domain.Account;
import org.apache.ibatis.type.MappedTypes;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MappedTypes(Account.class)
@MapperScan("com.tuum.testassignment.mapper")
@SpringBootApplication
public class TestassignmentApplication {

	public static void main(String[] args) {
		SpringApplication.run(TestassignmentApplication.class, args);
	}
}
