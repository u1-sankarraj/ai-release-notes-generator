package com.releasenotes.ai_release_notes.service;

import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.releasenotes.ai_release_notes.model.GithubWebhookRequest;
import com.releasenotes.ai_release_notes.queue.WebhookProducer;

@Service
public class WebhookService {

    private static final Logger log = LoggerFactory.getLogger(WebhookService.class);

    private final ObjectMapper objectMapper;

    @Value("${github.webhook.secret}")
    private String secret;

    private final WebhookProducer webhookProducer;

    private final Set<String> processedDeliveries = ConcurrentHashMap.newKeySet();

    public WebhookService(ObjectMapper objectMapper, WebhookProducer webhookProducer) {
        this.objectMapper = objectMapper;
        this.webhookProducer = webhookProducer;
    }

    public void handleWebhook(String event, String deliveryId, GithubWebhookRequest request, String payload) {

        log.debug("Processing webhook | deliveryId={} | event={}", deliveryId, event);

        // 1. Idempotency check
        if (processedDeliveries.contains(deliveryId)) {
            log.warn("Duplicate webhook ignored | deliveryId={}", deliveryId);
            return;
        }

        processedDeliveries.add(deliveryId);

        // 2. Event filter
        if (!"push".equals(event)) {
            log.info("Ignored non-push event | event={} | deliveryId={}", event, deliveryId);
            return;
        }

        // 3. Branch filter
        String branch = request.getRef().replace("refs/heads/", "");

        if (!"main".equals(branch)) {
            log.info("Ignored non-main branch | branch={} | deliveryId={}", branch, deliveryId);
            return;
        }

        // 4. Empty commits
        if (request.getCommits() == null || request.getCommits().isEmpty()) {
            log.warn("No commits found | deliveryId={}", deliveryId);
            return;
        }

        log.info("Valid webhook → sending to queue | deliveryId={} | commits={}",
                deliveryId, request.getCommits().size());

        // Send to queue
        webhookProducer.send(payload);
    }

    public boolean verifySignature(String payload, String signatureHeader) {
        try {
            if (signatureHeader == null) {
                log.error("Missing signature header");
                return false;
            }

            String expected = "sha256=" + hmacSha256(payload, secret);

            boolean valid = expected.equals(signatureHeader);

            if (!valid) {
                log.error("Signature mismatch");
            } else {
                log.debug("Signature verified successfully");
            }

            return valid;

        } catch (Exception e) {
            log.error("Error verifying signature", e);
            return false;
        }
    }

    private String hmacSha256(String data, String key) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "HmacSHA256");
        mac.init(secretKey);

        byte[] rawHmac = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));

        StringBuilder hex = new StringBuilder();
        for (byte b : rawHmac) {
            hex.append(String.format("%02x", b));
        }
        return hex.toString();
    }

    public GithubWebhookRequest parsePayload(String payload) {
        try {
            GithubWebhookRequest request = objectMapper.readValue(payload, GithubWebhookRequest.class);
            log.debug("Payload parsed successfully");
            return request;
        } catch (Exception e) {
            log.error("Invalid webhook payload", e);
            throw new RuntimeException("Invalid payload");
        }
    }
}