package com.releasenotes.ai_release_notes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.releasenotes.ai_release_notes.service.GitHubService;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private GitHubService gitHubService;

    @GetMapping("/release")
    public String testRelease() {

        String version = "v1.0." + System.currentTimeMillis();

        String notes = """
                ## 🚀 Test Release

                - Feature: Login API added
                - Fix: Authentication issue fixed
                - Docs: README updated
                """;

        gitHubService.createRelease(version, notes);

        return "Release triggered!";
    }
}