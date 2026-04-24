package com.releasenotes.ai_release_notes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling 
@EnableAsync
@SpringBootApplication
public class AiReleaseNotesApplication {

	public static void main(String[] args) {
		SpringApplication.run(AiReleaseNotesApplication.class, args);
		System.out.println("Started, checking in feature branch");
		System.out.println("Started, checking in feature branch going to commit");
	}

}
