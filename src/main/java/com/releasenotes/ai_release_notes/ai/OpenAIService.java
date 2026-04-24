package com.releasenotes.ai_release_notes.ai;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.releasenotes.ai_release_notes.model.GroqResponse;
import com.releasenotes.ai_release_notes.model.Message;
import com.releasenotes.ai_release_notes.model.ReleaseNotesResponse;
import com.releasenotes.ai_release_notes.entity.ReleaseNotesEntity;
import com.releasenotes.ai_release_notes.model.Choice;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;

import java.util.Map;
import java.util.ArrayList;
import java.util.List;

@Service
public class OpenAIService {

	private String API_KEY;

	public OpenAIService(@Value("${groq.api.key}") String apiKey) {
		this.API_KEY = apiKey;
	}
	
	public String generateReleaseNotes(String input) {
		System.out.println(API_KEY);
		String url = "https://api.groq.com/openai/v1/chat/completions";

		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBearerAuth(API_KEY);

		String prompt = """
				You are a senior software release manager.

				Your job is to transform raw git commit messages into HIGH-QUALITY, USER-FACING release notes similar to Jira, GitHub Releases, or enterprise SaaS products.

				CRITICAL INSTRUCTIONS:

				1. UNDERSTAND, DON'T REPEAT:
				- Do NOT copy commit messages
				- Interpret intent and rewrite meaningfully
				- Combine related commits into a single point

				2. MAINTAIN ORDER (VERY IMPORTANT):
				- Preserve the logical progression of commits based on input order
				- Earlier commits → foundational changes
				- Later commits → enhancements/fixes
				- Reflect evolution of the feature

				3. GROUP INTELLIGENTLY:
				Categorize into:
				- Highlights (most important changes only, max 3-5)
				- Features
				- Improvements
				- Bug Fixes
				- Documentation
				- Refactors
				- Others

				4. WRITE LIKE A PRODUCT:
				- Focus on USER IMPACT, not code
				- Use clear, professional language
				- Each point should feel like a release note, not a commit

				5. REMOVE NOISE:
				- Ignore duplicates
				- Ignore trivial commits
				- Merge similar ones

				6. KEEP IT CLEAN:
				- Use bullet points
				- Keep it concise but meaningful

				7. ADD STYLE:
				- Use light emojis for readability (not too many)
				- Use proper markdown formatting

				---

				OUTPUT FORMAT (STRICT):

				## 🚀 Highlights
				- ...

				## ✨ Features
				- ...

				## ⚡ Improvements
				- ...

				## 🐛 Bug Fixes
				- ...

				## 📚 Documentation
				- ...

				## 🔧 Refactors
				- ...

				## 📦 Others
				- ...

				---

				INPUT COMMITS (ordered):

				""" + input;

		Map<String, Object> message = Map.of("role", "user", "content", prompt);

		Map<String, Object> body = Map.of( "model", "llama-3.1-8b-instant", "messages", List.of(message));

		HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

		ResponseEntity<GroqResponse> response = restTemplate.postForEntity(url, request, GroqResponse.class);

		String content = response.getBody()
		        .getChoice()
		        .get(0)
		        .getMessage()
		        .getContent();
		if (content == null || content.isBlank()) {
		    throw new RuntimeException("AI returned empty response");
		}
		System.out.println(content+"dfdfsdfdsgsdge34567876543456789");
		
		return content;
	}
	
}