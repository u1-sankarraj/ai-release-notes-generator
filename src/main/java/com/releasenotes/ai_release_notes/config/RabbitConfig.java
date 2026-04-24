package com.releasenotes.ai_release_notes.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Bean
    public Queue webhookQueue() {
        return new Queue("webhook.queue", true);
    }
}
