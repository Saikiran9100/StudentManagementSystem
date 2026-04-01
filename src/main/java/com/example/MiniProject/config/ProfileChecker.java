package com.example.MiniProject.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ProfileChecker implements CommandLineRunner {

    @Value("${app.environment}")
    private String environment;

    @Override
    public void run(String... args) {
        System.out.println("Running Environment: " + environment);
    }
}