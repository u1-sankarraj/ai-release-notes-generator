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

	public ReleaseNotesResponse generateReleaseNotes(String input) {
		System.out.println(API_KEY);
		String url = "https://api.groq.com/openai/v1/chat/completions";

		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBearerAuth(API_KEY);

		String prompt = "Convert the following release notes into professional format:\n\n" + input;

		Map<String, Object> message = Map.of("role", "user", "content", prompt);

		Map<String, Object> body = Map.of( "model", "llama-3.1-8b-instant", "messages", List.of(message));

		HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

		ResponseEntity<GroqResponse> response = restTemplate.postForEntity(url, request, GroqResponse.class);

		String content = response.getBody()
		        .getChoice()
		        .get(0)
		        .getMessage()
		        .getContent();
		
		ReleaseNotesResponse parsed = parse(content);
		return parsed;
	}
	
	public ReleaseNotesResponse parse(String content) {
  
        List<String> features = new ArrayList<>();
        List<String> fixes = new ArrayList<>();
        List<String> docs = new ArrayList<>();
        List<String> refactors = new ArrayList<>();

        String current = "";

        for (String line : content.split("\n")) {

            if (line.contains("Features")) current = "features";
            else if (line.contains("Bug Fixes")) current = "fixes";
            else if (line.contains("Documentation")) current = "docs";
            else if (line.contains("Refactors")) current = "refactors";

            else if (line.startsWith("*") || line.startsWith("-")) {
                String value = line.replace("*", "").replace("-", "").trim();

                switch (current) {
                    case "features" -> features.add(value);
                    case "fixes" -> fixes.add(value);
                    case "docs" -> docs.add(value);
                    case "refactors" -> refactors.add(value);
                }
            }
        }

        ReleaseNotesResponse res = new ReleaseNotesResponse();

        res.setFeatures(new ArrayList<>());
        res.setFixes(new ArrayList<>());
        res.setDocs(new ArrayList<>());
        res.setRefactors(new ArrayList<>());
        res.setOthers(new ArrayList<>());

        return res;
    }
}