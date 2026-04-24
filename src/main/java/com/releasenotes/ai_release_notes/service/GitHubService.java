package com.releasenotes.ai_release_notes.service;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GitHubService {

    private static final Logger log = LoggerFactory.getLogger(GitHubService.class);

    @Value("${github.token}")
    private String token;

    @Value("${github.repo.owner}")
    private String owner;

    @Value("${github.repo.name}")
    private String repo;

    private final RestTemplate restTemplate = new RestTemplate();

    public void createRelease(String version, String notes) {

        String url = "https://api.github.com/repos/" + owner + "/" + repo + "/releases";

        log.info("🚀 Creating GitHub release | repo={}/{} | version={}", owner, repo, version);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = Map.of(
                "tag_name", version,
                "name", version,
                "body", notes,
                "draft", false,
                "prerelease", false
        );

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response =
                    restTemplate.postForEntity(url, request, String.class);

            log.info("✅ GitHub release created successfully | status={}", response.getStatusCode());

        } catch (Exception e) {
            log.error("❌ Failed to create GitHub release | version={}", version, e);
            throw e; // important: propagate failure
        }
    }
}