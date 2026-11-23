package com.example.simplestcrud;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
public class SimplestcrudApplication {

    public static void main(String[] args) {
        SpringApplication.run(SimplestcrudApplication.class, args);
    }

    // This @Bean tells Spring Boot to run this code right after startup
    @Bean
    public CommandLineRunner testDatabaseConnection(JdbcTemplate jdbcTemplate) {
        return args -> {
            System.out.println("=== TESTING DATABASE CONNECTION ===");
            // This is raw SQL. It asks the database "What is your version?"
            String version = jdbcTemplate.queryForObject("SELECT H2VERSION()", String.class);
            System.out.println("Successfully connected to H2 Database! Version: " + version);
            System.out.println("===================================");
        };
    }
}