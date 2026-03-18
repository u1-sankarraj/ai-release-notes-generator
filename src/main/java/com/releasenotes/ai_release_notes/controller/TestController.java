package com.releasenotes.ai_release_notes.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TestController {

    @GetMapping("/test")
    public String test() {
        return "Backend is working";
    }
    
    @PostMapping("/Post")
    public String test1(@RequestBody String name) {
    	System.out.println(name);
		return "post mapping";
    	
    }
}
