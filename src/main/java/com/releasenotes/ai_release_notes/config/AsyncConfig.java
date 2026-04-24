package com.releasenotes.ai_release_notes.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean("taskExecutor")
    public Executor taskExecutor() {

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        executor.setCorePoolSize(5);     // always running threads
        executor.setMaxPoolSize(20);     // max threads
        executor.setQueueCapacity(50);   // waiting queue
        executor.setThreadNamePrefix("webhook-");

        executor.initialize();
        return executor;
    }
}
