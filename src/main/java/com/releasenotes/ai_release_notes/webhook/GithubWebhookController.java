package com.releasenotes.ai_release_notes.webhook;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.releasenotes.ai_release_notes.model.GithubWebhookRequest;
import com.releasenotes.ai_release_notes.service.CommitProcessingService;
import com.releasenotes.ai_release_notes.service.WebhookService;

@RestController
@RequestMapping("/webhook")
public class GithubWebhookController {

    private static final Logger log = LoggerFactory.getLogger(GithubWebhookController.class);

    private final CommitProcessingService commitservice;
    private final WebhookService webhookService;
    private final Map<String, Integer> requestCount = new ConcurrentHashMap<>();
    private long lastResetTime = System.currentTimeMillis();

    public GithubWebhookController(CommitProcessingService service, WebhookService webhookService) {
        this.commitservice = service;
        this.webhookService = webhookService;
    }

    @PostMapping("/github")
    public ResponseEntity<Map<String, String>> receiveWebhook(
            @RequestHeader(value = "X-GitHub-Event", required = false) String event,
            @RequestHeader(value = "X-GitHub-Delivery", required = false) String deliveryId,
            @RequestHeader(value = "X-Hub-Signature-256", required = false) String signature,
            @RequestBody String payload) {

        log.info("📩 Webhook received | event={} | deliveryId={}", event, deliveryId);

        // Rate limit check
        if (isRateLimited()) {
            log.warn("🚫 Rate limit exceeded | deliveryId={}", deliveryId);
            return ResponseEntity.status(429).body(Map.of("status", "rate_limited"));
        }

        // Signature validation
        if (signature == null || !webhookService.verifySignature(payload, signature)) {
            log.error("❌ Invalid or missing signature | deliveryId={}", deliveryId);
            return ResponseEntity.status(401).body(Map.of("status", "unauthorized"));
        }

        try {
            // Parse payload
            GithubWebhookRequest request = webhookService.parsePayload(payload);
            log.debug("Payload parsed successfully | deliveryId={}", deliveryId);

            // Process webhook
            webhookService.handleWebhook(event, deliveryId, request, payload);

            log.info("✅ Webhook accepted for processing | deliveryId={}", deliveryId);

            return ResponseEntity.ok(Map.of("status", "received"));

        } catch (Exception e) {
            log.error("🔥 Error processing webhook | deliveryId={}", deliveryId, e);
            return ResponseEntity.status(500).body(Map.of("status", "error"));
        }
    }

    private boolean isRateLimited() {

        long currentTime = System.currentTimeMillis();

        if (currentTime - lastResetTime > 60000) {
            requestCount.clear();
            lastResetTime = currentTime;
            log.debug("Rate limiter reset");
        }

        String key = "global";

        requestCount.merge(key, 1, Integer::sum);

        int count = requestCount.get(key);

        log.debug("Rate limiter count={}", count);

        return count > 10;
    }
}