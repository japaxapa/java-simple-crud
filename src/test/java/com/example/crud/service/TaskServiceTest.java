package com.example.crud.service;

import com.example.crud.model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TaskServiceTest {

    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskService = new TaskService();
    }

    @Test
    void createShouldAssignIdAndStoreTask() {
        Task newTask = new Task("Learn Java");

        Task createdTask = taskService.create(newTask);

        assertNotNull(createdTask.getId(), "ID should not be null after creation");
        assertEquals(1L, createdTask.getId(), "First ID should be 1");
        assertEquals("Learn Java", createdTask.getTitle());
        assertFalse(createdTask.isDone());
    }

    @Test
    void createMultipleTasksShouldIncrementId() {
        Task task1 = new Task("Task 1");
        Task task2 = new Task("Task 2");
        Task task3 = new Task("Task 3");

        taskService.create(task1);
        taskService.create(task2);
        taskService.create(task3);

        assertEquals(1L, task1.getId());
        assertEquals(2L, task2.getId());
        assertEquals(3L, task3.getId());
    }

    @Test
    void createNullTaskShouldThrowException() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> taskService.create(null)
        );
        assertEquals("Task cannot be null", exception.getMessage());
    }

    @Test
    void createBlankTitleShouldThrowException() {
        Task taskWithNullTitle = new Task((String) null);
        Task taskWithEmptyTitle = new Task("");
        Task taskWithSpacesTitle = new Task("     ");

        assertThrows(IllegalArgumentException.class, () -> taskService.create(taskWithNullTitle));
        assertThrows(IllegalArgumentException.class, () -> taskService.create(taskWithEmptyTitle));
        assertThrows(IllegalArgumentException.class, () -> taskService.create(taskWithSpacesTitle));
    }
}
