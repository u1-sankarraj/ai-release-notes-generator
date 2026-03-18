package com.releasenotes.ai_release_notes.service;

import java.util.*;

import org.springframework.stereotype.Service;

import com.releasenotes.ai_release_notes.entity.ReleaseNotesEntity;
import com.releasenotes.ai_release_notes.model.Commit;
import com.releasenotes.ai_release_notes.model.ReleaseNotesResponse;
import com.releasenotes.ai_release_notes.repository.ReleaseNotesRepository;

@Service
public class CommitProcessingService {
	private final ReleaseNotesRepository repository;

	public CommitProcessingService(ReleaseNotesRepository repository) {
		this.repository = repository;
	}

	public ReleaseNotesResponse process(List<Commit> commits) {
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

		for (Commit c : commits) {

			String message = c.getMessage();
			String[] parts = message.split(":");

			String type = parts[0];
			String description = parts.length > 1 ? parts[1].trim() : message;

			String category = typeMap.getOrDefault(type, "Others");

			categorisedCommits.get(category).add(description);
		}

		ReleaseNotesResponse response = new ReleaseNotesResponse();

		response.setFeatures(categorisedCommits.get("Features"));
		response.setFixes(categorisedCommits.get("Fixes"));
		response.setDocs(categorisedCommits.get("Docs"));
		response.setRefactors(categorisedCommits.get("Refactors"));
		response.setOthers(categorisedCommits.get("Others"));

		ReleaseNotesEntity entity = new ReleaseNotesEntity();

		entity.setFeatures(String.join(", ", response.getFeatures()));
		entity.setFixes(String.join(", ", response.getFixes()));
		entity.setDocs(String.join(", ", response.getDocs()));
		entity.setRefactors(String.join(", ", response.getRefactors()));
		entity.setOthers(String.join(", ", response.getOthers()));

		repository.save(entity);

		return response;

	}

	public List<ReleaseNotesEntity> getAllReleaseNotes() {
		return repository.findAll();
	}

	public ReleaseNotesEntity getLatestReleaseNotes() {
		return repository.findTopByOrderByIdDesc();
	}

}