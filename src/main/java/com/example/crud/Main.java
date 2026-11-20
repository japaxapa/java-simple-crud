package com.example.crud;

import com.example.crud.model.Task;
import com.example.crud.service.TaskService;

import java.util.List;
import java.util.Optional;

public class Main {

    static void main(String[] args) {
        TaskService taskService = new TaskService();

        System.out.println("=== Creating 3 tasks ===");
        Task task1 = taskService.create(new Task("Learn Java"));
        Task task2 = taskService.create(new Task("Build a CRUD app"));
        Task task3 = taskService.create(new Task("Write unit tests"));
        System.out.println("Created: " + task1);
        System.out.println("Created: " + task2);
        System.out.println("Created: " + task3);

        System.out.println("\n=== All tasks ===");
        List<Task> allTasks = taskService.findAll();
        allTasks.forEach(System.out::println);

        System.out.println("\n=== Updating task #2");
        Task updatePayload = new Task("Build a REAL CRUD app");
        updatePayload.setDone(true);
        Task updated = taskService.update(task2.getId(), updatePayload);
        System.out.println("Updated: " + updated);

        System.out.println("\n=== Deleting task #1 ===");
        boolean deleted = taskService.delete(task1.getId());
        System.out.println("Deleted task #1: " + deleted);

        System.out.println("\n=== findById for deleted ID (" + task1.getId() + ") ===");
        Optional<Task> deletedLookup = taskService.findById(task1.getId());
        if (deletedLookup.isPresent()) {
            System.out.println("Found: " + deletedLookup.get());
        } else {
            System.out.println("Not found (as expected).");
        }

        System.out.println("\n=== findById for existing ID (" + task3.getId() + ") ===");
        Optional<Task> existingLookup = taskService.findById(task3.getId());
        if (existingLookup.isPresent()) {
            System.out.println("Found: " + existingLookup.get());
        } else {
            System.out.println("Not found (unexpected!).");
        }

        System.out.println("\n=== Final state of all tasks ===");
        taskService.findAll().forEach(System.out::println);
    }
}
