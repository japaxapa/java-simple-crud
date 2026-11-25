package com.example.crud.service;

import com.example.crud.repository.TaskRepository;
import com.example.crud.exception.TaskNotFoundException;
import com.example.crud.model.Task;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    // CREATE TASK METHOD
    public Task create(Task task) {
        if (task == null) {
            throw new IllegalArgumentException("Task cannot be null");
        }

        if (task.getTitle() == null || task.getTitle().isBlank()) {
            throw new IllegalArgumentException("Task title cannot be blank");
        }

        return taskRepository.save(task);
    }

    // FIND METHODS
    public Optional<Task> findById(Long id) {
        if (id == null) {
            return Optional.empty();
        }

        return taskRepository.findById(id);
    }

    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    // UPDATE METHOD
    public Task update(Long id, Task updated) {
        Task existingTask = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));

        if (updated == null || updated.getTitle() == null || updated.getTitle().isBlank()) {
            throw new IllegalArgumentException("Task title cannot be blank");
        }

        existingTask.setTitle(updated.getTitle());
        existingTask.setDone(updated.isDone());

        return taskRepository.save(existingTask);
    }

    // DELETE METHOD
    public boolean delete(Long id) {
        if (id == null) {
            return false;
        }

        if (taskRepository.existsById(id)) {
            taskRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
