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
}
