package com.tuum.testassignment.serviceIntegrationTests;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
class IntegrationTestBase {
    @Container
    static final PostgreSQLContainer<?> postgresContainer;
    @Container
    static final RabbitMQContainer rabbitMQContainer;

    static {
        postgresContainer = new PostgreSQLContainer<>("postgres:14")
                .withDatabaseName("tuum-db")
                .withUsername("tuum")
                .withPassword("password")
                .withInitScript("schema.sql")
                .withReuse(true);
        postgresContainer.start();

        rabbitMQContainer =
                new RabbitMQContainer("rabbitmq:3.10.1").withVhost("/")
                        .withExposedPorts(5672, 15672)
                        .withUser("admin", "admin")
                        .withPermission("/", "admin", ".*", ".*", ".*")
                        .withReuse(true);
        rabbitMQContainer.start();
    }
}


