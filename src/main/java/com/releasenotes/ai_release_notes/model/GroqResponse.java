package com.releasenotes.ai_release_notes.model;


import java.util.List;

public class GroqResponse {
	private List<Choice> choice;

	public List<Choice> getChoice() {
		return choice;
	}

	public void setChoices(List<Choice> choice) {
		this.choice = choice;
	}
}
