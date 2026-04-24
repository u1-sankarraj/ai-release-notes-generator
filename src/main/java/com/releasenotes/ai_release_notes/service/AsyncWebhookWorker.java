package com.releasenotes.ai_release_notes.service;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.releasenotes.ai_release_notes.entity.FailedWebhookEvent;
import com.releasenotes.ai_release_notes.model.GithubWebhookRequest;
import com.releasenotes.ai_release_notes.repository.FailedWebhookRepository;

@Service
public class AsyncWebhookWorker {

    private static final Logger log = LoggerFactory.getLogger(AsyncWebhookWorker.class);

    private final CommitProcessingService commitProcessingService;
    private final FailedWebhookRepository failedRepo;

    public AsyncWebhookWorker(CommitProcessingService service, FailedWebhookRepository failedRepo) {
        this.commitProcessingService = service;
        this.failedRepo = failedRepo;
    }

    @Async("taskExecutor")
    public CompletableFuture<Void> process(GithubWebhookRequest request, String rawPayload) {

        log.info("🚀 Async processing started | commits={}",
                request.getCommits() != null ? request.getCommits().size() : 0);

        try {

            commitProcessingService.process(request.getCommits());

            log.info("✅ Async processing completed successfully");

        } catch (Exception e) {

            log.error("❌ Processing failed, storing event for retry", e);

            FailedWebhookEvent failed = new FailedWebhookEvent();
            failed.setPayload(rawPayload);
            failed.setError(e.getMessage());
            failed.setCreatedAt(LocalDateTime.now());

            failedRepo.save(failed);

            log.warn("⚠️ Failed event stored in DB for retry");
        }

        return CompletableFuture.completedFuture(null);
    }
}