package com.example.simplestcrud;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;

@SpringBootApplication(scanBasePackages = {"com.example.crud", "com.example.simplestcrud"})
@EntityScan(basePackages = "com.example.crud.model") // Tells Hibernate where the Task class is
public class SimplestcrudApplication {

    static void main(String[] args) {
        SpringApplication.run(SimplestcrudApplication.class, args);
    }

    @Bean
    public CommandLineRunner checkDatabase(JdbcTemplate jdbcTemplate) {
        return args -> {
            System.out.println("=== CHECKING IF TABLE WAS CREATED ===");

            // Raw SQL to check if Hibernate created the table
            String sql = "SELECT COLUMN_NAME, DATA_TYPE FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'TASKS'";
            List<Map<String, Object>> columns = jdbcTemplate.queryForList(sql);

            if (columns.isEmpty()) {
                System.out.println("❌ Table 'TASKS' does not exist yet!");
            } else {
                System.out.println("✅ Table 'TASKS' was created with the following columns:");
                for (Map<String, Object> column : columns) {
                    System.out.println(" - " + column.get("COLUMN_NAME") + " (" + column.get("DATA_TYPE") + ")");
                }
            }
            System.out.println("===================================");
        };
    }
}