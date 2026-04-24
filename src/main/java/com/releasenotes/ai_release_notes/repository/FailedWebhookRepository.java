package com.releasenotes.ai_release_notes.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.releasenotes.ai_release_notes.entity.FailedWebhookEvent;

public interface FailedWebhookRepository extends JpaRepository<FailedWebhookEvent, Long> {

    List<FailedWebhookEvent> findByStatus(String status); // existing
    List<FailedWebhookEvent> findByStatusAndRetryCountLessThan(String status, int maxRetry);
}
