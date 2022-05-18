package com.tuum.testassignment.messageQueue;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${spring.rabbitmq.queue}")
    String queueName;

    @Bean
    Queue queue() {
        return new Queue(queueName);
    }
}
