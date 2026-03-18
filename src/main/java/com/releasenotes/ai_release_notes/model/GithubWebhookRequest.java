package com.releasenotes.ai_release_notes.model;

import java.util.List;

public class GithubWebhookRequest {

    private String ref;
    private List<Commit> commits;

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

	public List<Commit> getCommits() {
        return commits;
    }

    public void setCommits(List<Commit> commits) {
        this.commits = commits;
    }
}