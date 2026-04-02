package com.releasenotes.ai_release_notes.webhook;

import java.util.Map;

import org.springframework.web.bind.annotation.*;
import com.releasenotes.ai_release_notes.model.GithubWebhookRequest;
import com.releasenotes.ai_release_notes.model.ReleaseNotesResponse;
import com.releasenotes.ai_release_notes.service.CommitProcessingService;

@RestController
@RequestMapping("/webhook")
public class GithubWebhookController {

	private final CommitProcessingService commitservice;

	public GithubWebhookController(CommitProcessingService service) {
		this.commitservice = service;

	}

	@PostMapping("/github")
	public Map<String, String> receiveWebhook(@RequestBody GithubWebhookRequest request) {
		return commitservice.process(request.getCommits());
	}
}