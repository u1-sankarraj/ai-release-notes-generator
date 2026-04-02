package com.releasenotes.ai_release_notes.service;

import java.util.*;

import org.springframework.stereotype.Service;

import com.releasenotes.ai_release_notes.ai.OpenAIService;
import com.releasenotes.ai_release_notes.entity.ReleaseNotesEntity;
import com.releasenotes.ai_release_notes.model.Commit;
import com.releasenotes.ai_release_notes.model.ReleaseNotesResponse;
import com.releasenotes.ai_release_notes.repository.ReleaseNotesRepository;

@Service
public class CommitProcessingService {

    private final ReleaseNotesRepository repository;
    private final OpenAIService openAIService;
    
    public CommitProcessingService(ReleaseNotesRepository repository, OpenAIService openAIService) {
        this.repository = repository;
		this.openAIService = openAIService;
    }

    public Map<String, String> process(List<Commit> commits) {
    	System.out.println("🔥 PROCESS METHOD CALLED");
        Map<String, List<String>> categorisedCommits = new HashMap<>();
        categorisedCommits.put("Features", new ArrayList<>());
        categorisedCommits.put("Fixes", new ArrayList<>());
        categorisedCommits.put("Docs", new ArrayList<>());
        categorisedCommits.put("Refactors", new ArrayList<>());
        categorisedCommits.put("Others", new ArrayList<>());

        Map<String, String> typeMap = new HashMap<>();
        typeMap.put("feat", "Features");
        typeMap.put("ref", "Refactors");
        typeMap.put("docs", "Docs");
        typeMap.put("fix", "Fixes");

        // 🔥 MAIN LOOP (processing commits)
        for (Commit c : commits) {

            String message = c.getMessage();

            String[] parts = message.split(":");

            String type = parts[0];

            String rawDescription = parts.length > 1 ? parts[1] : message;

            String description = cleanMessage(rawDescription);

            String category = typeMap.getOrDefault(type, "Others");

            List<String> list = categorisedCommits.get(category);

            if (!list.contains(description)) {
                list.add(description);
            }
        }

        // Build response DTO
        ReleaseNotesResponse response = new ReleaseNotesResponse();

        response.setFeatures(categorisedCommits.get("Features"));
        response.setFixes(categorisedCommits.get("Fixes"));
        response.setDocs(categorisedCommits.get("Docs"));
        response.setRefactors(categorisedCommits.get("Refactors"));
        response.setOthers(categorisedCommits.get("Others"));

        // FORMAT OUTPUT
        String formattedNotes = buildReleaseNotes(response);
        String aiOutput = openAIService.generateReleaseNotes(formattedNotes);
        
        
        System.out.println("Formatted Notes" + formattedNotes);
        System.out.println("open AI op" + aiOutput.toString());

        // Save to DB (current approach)
        ReleaseNotesEntity entity = new ReleaseNotesEntity();

        entity.setFeatures(String.join(", ", response.getFeatures()));
        entity.setFixes(String.join(", ", response.getFixes()));
        entity.setDocs(String.join(", ", response.getDocs()));
        entity.setRefactors(String.join(", ", response.getRefactors()));
        entity.setOthers(String.join(", ", response.getOthers()));
        entity.setAiNotes(aiOutput);
        repository.save(entity);
        return Map.of("releasenotes",aiOutput);
    }

    // 🔥 CLEAN MESSAGE METHOD
    private String cleanMessage(String message) {

        message = message.trim();

        if (message.isEmpty()) return message;

        message = message.substring(0, 1).toUpperCase() + message.substring(1);

        if (message.startsWith("Add ")) {
            message = message.replaceFirst("Add", "Added");
        } else if (message.startsWith("Fix ")) {
            message = message.replaceFirst("Fix", "Fixed");
        } else if (message.startsWith("Update ")) {
            message = message.replaceFirst("Update", "Updated");
        }

        return message;
    }

    // 🔥 FORMATTER METHOD
    private String buildReleaseNotes(ReleaseNotesResponse response) {

        Map<String, List<String>> sections = new LinkedHashMap<>();

        sections.put("✨ Features", response.getFeatures());
        sections.put("🐞 Bug Fixes", response.getFixes());
        sections.put("📚 Documentation", response.getDocs());
        sections.put("🔧 Refactors", response.getRefactors());
        sections.put("📦 Others", response.getOthers());

        StringBuilder sb = new StringBuilder();
        sb.append("## 🚀 Release Notes\n\n");

        for (Map.Entry<String, List<String>> entry : sections.entrySet()) {

            List<String> items = entry.getValue();

            if (items == null || items.isEmpty()) continue;

            sb.append("### ").append(entry.getKey()).append("\n");

            for (String item : items) {
                sb.append("- ").append(item).append("\n");
            }

            sb.append("\n");
        }

        return sb.toString();
    }
    
    
    // GET APIs
    public List<ReleaseNotesEntity> getAllReleaseNotes() {
        return repository.findAll();
    }

    public ReleaseNotesEntity getLatestReleaseNotes() {
        return repository.findTopByOrderByIdDesc();
    }
}