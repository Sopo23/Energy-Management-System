package com.example.device_service.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String DEVICE_CHANGES_QUEUE = "device_changes";

    @Bean
    public Queue deviceChangesQueue() {
        return new Queue(DEVICE_CHANGES_QUEUE, true);
    }
}
