package com.example.crud.service;

import com.example.crud.model.Task;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class TaskService {
    private final Map<Long, Task> tasks = new HashMap<>();

    private final AtomicLong idGenerator = new AtomicLong(0);

    // NOTE ON THREAD SAFETY:
    // HashMap is not thread-safe. If multiple threads try to modify this map
    // at the exact same time, it can cause data corruption.
    // Since this is a simple single-threaded learning project, we accept this risk.
    // In a real multi-threaded application, we would use ConcurrentHashMap
    // or make our methods "synchronized".

    // CREATE TASK METHOD
    public Task create(Task task) {
        if (task == null) {
            throw new IllegalArgumentException("Task cannot be null");
        }

        if (task.getTitle() == null || task.getTitle().isBlank()) {
            throw new IllegalArgumentException("Task title cannot be blank");
        }

        Long newId = idGenerator.incrementAndGet();
        task.setId(newId);

        tasks.put(newId, task);

        return task;
    }

}
