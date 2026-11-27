package com.example.crud.controller;

import com.example.crud.exception.TaskNotFoundException;
import com.example.crud.model.Task;
import com.example.crud.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    // GET /api/tasks — list all
    @GetMapping
    public List<Task> getAllTasks() {
        return taskService.findAll();
    }

    // GET /api/tasks/{id} — get one
    @GetMapping("/{id}")
    public Task getTaskById(@PathVariable Long id) {
        return taskService.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
    }

    // POST /api/tasks — create
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)   // 201, not 200
    public Task createTask(@RequestBody Task task) {
        return taskService.create(task);
    }

    // PUT /api/tasks/{id} — update
    @PutMapping("/{id}")
    public Task updateTask(@PathVariable Long id, @RequestBody Task task) {
        return taskService.update(id, task);
    }

    // DELETE /api/tasks/{id} — delete
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)  // 204, not 200
    public void deleteTask(@PathVariable Long id) {
        taskService.delete(id);
    }
}
