package com.releasenotes.ai_release_notes.queue;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class WebhookProducer {

    private final RabbitTemplate rabbitTemplate;

    public WebhookProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void send(String payload) {

        System.out.println("📤 Sending to queue...");

        rabbitTemplate.convertAndSend("webhook.queue", payload);
    }
}