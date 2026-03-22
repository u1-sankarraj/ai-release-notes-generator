package com.releasenotes.ai_release_notes.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.releasenotes.ai_release_notes.entity.ReleaseNotesEntity;
import com.releasenotes.ai_release_notes.service.CommitProcessingService;

@RestController
@RequestMapping("/release-notes")
public class ReleaseNotesController {

    private final CommitProcessingService service;

    public ReleaseNotesController(CommitProcessingService service) {
        this.service = service;
    }

    @GetMapping
    public List<ReleaseNotesEntity> getReleaseNotes() {
        return service.getAllReleaseNotes();
    }
    
    @GetMapping("/latest")
    public ReleaseNotesEntity getLatestReleaseNotes() {
        return service.getLatestReleaseNotes();
    }
} 
