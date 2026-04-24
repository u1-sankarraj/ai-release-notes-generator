package com.releasenotes.ai_release_notes.scheduler;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.releasenotes.ai_release_notes.entity.FailedWebhookEvent;
import com.releasenotes.ai_release_notes.model.GithubWebhookRequest;
import com.releasenotes.ai_release_notes.repository.FailedWebhookRepository;
import com.releasenotes.ai_release_notes.service.CommitProcessingService;

@Component
public class FailedEventRetryScheduler {

    private final FailedWebhookRepository repo;
    private final CommitProcessingService processingService;
    private final ObjectMapper objectMapper;

    public FailedEventRetryScheduler(FailedWebhookRepository repo,
                                     CommitProcessingService processingService,
                                     ObjectMapper objectMapper) {
        this.repo = repo;
        this.processingService = processingService;
        this.objectMapper = objectMapper;
    }

    @Scheduled(fixedDelay = 30000)
    public void retryFailedEvents() {

        List<FailedWebhookEvent> events =
                repo.findByStatusAndRetryCountLessThan("FAILED", 3);

        for (FailedWebhookEvent event : events) {

            // ⛔ Skip if recently tried (cooldown)
            if (event.getLastTriedAt() != null &&
                event.getLastTriedAt().isAfter(LocalDateTime.now().minusSeconds(60))) {
                continue;
            }

            try {
                System.out.println("Retrying event ID: " + event.getId());

                GithubWebhookRequest request =
                        objectMapper.readValue(event.getPayload(), GithubWebhookRequest.class);

                processingService.process(request.getCommits());

                event.setStatus("SUCCESS");

            } catch (Exception e) {

                event.setRetryCount(event.getRetryCount() + 1);
                event.setLastTriedAt(LocalDateTime.now());

                if (event.getRetryCount() >= 3) {
                    event.setStatus("FAILED_FINAL");
                }
            }

            repo.save(event);
        }
    }
}