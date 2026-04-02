package com.releasenotes.ai_release_notes.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "release_notes_entity") // keep same as your DB
public class ReleaseNotesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String features;
    private String fixes;
    private String docs;
    private String refactors;
    private String others;

    @Column(columnDefinition = "TEXT")
    private String aiNotes; // ⭐ AI output (LONG TEXT)

    @Column(columnDefinition = "TEXT")
    private String rawCommits; // ⭐ optional (future use)

    @Column(columnDefinition = "TEXT")
    private String formattedNotes; // ⭐ optional

    private LocalDateTime createdAt = LocalDateTime.now();

    // ===== GETTERS & SETTERS =====

    public Long getId() {
        return id;
    }

    public String getFeatures() {
        return features;
    }

    public void setFeatures(String features) {
        this.features = features;
    }

    public String getFixes() {
        return fixes;
    }

    public void setFixes(String fixes) {
        this.fixes = fixes;
    }

    public String getDocs() {
        return docs;
    }

    public void setDocs(String docs) {
        this.docs = docs;
    }

    public String getRefactors() {
        return refactors;
    }

    public void setRefactors(String refactors) {
        this.refactors = refactors;
    }

    public String getOthers() {
        return others;
    }

    public void setOthers(String others) {
        this.others = others;
    }

    public String getAiNotes() {
        return aiNotes;
    }

    public void setAiNotes(String aiNotes) {
        this.aiNotes = aiNotes;
    }

    public String getRawCommits() {
        return rawCommits;
    }

    public void setRawCommits(String rawCommits) {
        this.rawCommits = rawCommits;
    }

    public String getFormattedNotes() {
        return formattedNotes;
    }

    public void setFormattedNotes(String formattedNotes) {
        this.formattedNotes = formattedNotes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}