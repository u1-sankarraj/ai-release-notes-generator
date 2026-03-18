package com.releasenotes.ai_release_notes.model;

import java.util.List;

public class ReleaseNotesResponse {

	private List<String> features;
	private List<String> fixes;
	private List<String> docs;
	private List<String> refactors;
	private List<String> others;

	public List<String> getFeatures() {
		return features;
	}

	public void setFeatures(List<String> features) {
		this.features = features;
	}

	public List<String> getFixes() {
		return fixes;
	}

	public void setFixes(List<String> fixes) {
		this.fixes = fixes;
	}

	public List<String> getDocs() {
		return docs;
	}

	public void setDocs(List<String> docs) {
		this.docs = docs;
	}

	public List<String> getRefactors() {
		return refactors;
	}

	public void setRefactors(List<String> refactors) {
		this.refactors = refactors;
	}

	public List<String> getOthers() {
		return others;
	}

	public void setOthers(List<String> others) {
		this.others = others;
	}
}