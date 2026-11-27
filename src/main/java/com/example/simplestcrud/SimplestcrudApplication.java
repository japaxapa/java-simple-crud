package com.example.simplestcrud;

import com.example.crud.model.Task;
import com.example.crud.service.TaskService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"com.example.simplestcrud", "com.example.crud"})
@EnableJpaRepositories(basePackages = "com.example.crud.repository")
@EntityScan("com.example.crud.model")
public class SimplestcrudApplication {

    static void main(String[] args) {
        SpringApplication.run(SimplestcrudApplication.class, args);
    }

    @Bean
    public CommandLineRunner demoDatabase(TaskService taskService) {
        return args -> {
            System.out.println("=== TESTING DATABASE CRUD ===");

            // 1. Create
            Task newTask = new Task("Learn Spring Data JPA");
            Task savedTask = taskService.create(newTask);
            System.out.println("✅ Saved to DB: " + savedTask);

            // 2. Find All
            System.out.println("✅ All tasks in DB: " + taskService.findAll());

            // 3. Update
            Task updatePayload = new Task("Learn Spring Data JPA (Updated)");
            updatePayload.setDone(true);
            Task updatedTask = taskService.update(savedTask.getId(), updatePayload);
            System.out.println("✅ Updated in DB: " + updatedTask);

            // 4. Delete
            boolean deleted = taskService.delete(updatedTask.getId());
            System.out.println("✅ Deleted from DB: " + deleted);

            System.out.println("✅ Tasks remaining: " + taskService.findAll());
            System.out.println("=============================");
        };
    }
}