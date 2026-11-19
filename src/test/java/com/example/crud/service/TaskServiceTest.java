package com.example.crud.service;

import com.example.crud.exception.TaskNotFoundException;
import com.example.crud.model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class TaskServiceTest {

    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskService = new TaskService();
    }

    @Test
    void create_ShouldAssignIdAndStoreTask() {
        Task newTask = new Task("Learn Java");

        Task createdTask = taskService.create(newTask);

        assertNotNull(createdTask.getId(), "ID should not be null after creation");
        assertEquals(1L, createdTask.getId(), "First ID should be 1");
        assertEquals("Learn Java", createdTask.getTitle());
        assertFalse(createdTask.isDone());
    }

    @Test
    void create_MultipleTasksShouldIncrementId() {
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
    void create_NullTaskShouldThrowException() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> taskService.create(null)
        );
        assertEquals("Task cannot be null", exception.getMessage());
    }

    @Test
    void create_BlankTitleShouldThrowException() {
        Task taskWithNullTitle = new Task((String) null);
        Task taskWithEmptyTitle = new Task("");
        Task taskWithSpacesTitle = new Task("     ");

        assertThrows(IllegalArgumentException.class, () -> taskService.create(taskWithNullTitle));
        assertThrows(IllegalArgumentException.class, () -> taskService.create(taskWithEmptyTitle));
        assertThrows(IllegalArgumentException.class, () -> taskService.create(taskWithSpacesTitle));
    }

    @Test
    void findById_ExistingIdShouldReturnTask() {
        Task taskToCreate = new Task("find me");
        Task createdTask = taskService.create(taskToCreate);

        Optional<Task> result = taskService.findById(createdTask.getId());

        assertTrue(result.isPresent(), "Optional should contain a task");
        assertEquals(createdTask, result.get(), "Retrieved task should match created task");
    }

    @Test
    void findById_MissingIdShouldReturnEmpty() {
        Optional<Task> result = taskService.findById(999L);

        assertTrue(result.isEmpty(), "Optional should be empty for a missing ID");
    }

    @Test
    void findById_NullIdShouldReturnEmpty() {
        Optional<Task> result = taskService.findById(null);
        assertTrue(result.isEmpty(), "Optional should be empty for a null ID");
    }

    @Test
    void findAll_EmptyStoreShouldReturnEmptyList() {
        List<Task> result = taskService.findAll();

        assertNotNull(result, "List should not be null");
        assertTrue(result.isEmpty(), "List should be empty");
    }

    @Test
    void findAll_MultipleTasksShouldReturnInInsertionOrder() {
        Task task1 = new Task("First");
        Task task2 = new Task("Second");
        Task task3 = new Task("Third");
        taskService.create(task1);
        taskService.create(task2);
        taskService.create(task3);

        List<Task> result = taskService.findAll();

        assertEquals(3, result.size());
        assertEquals("First", result.get(0).getTitle(), "First item should be 'First'");
        assertEquals("Second", result.get(1).getTitle(), "Second item should be 'Second'");
        assertEquals("Third", result.get(2).getTitle(), "Third item should be 'Third'");
    }

    @Test
    void findAll_MutatingReturnedListShouldNotAffectStore() {
        taskService.create(new Task("Task 1"));
        taskService.create(new Task("Task 2"));

        List<Task> result = taskService.findAll();
        result.clear();

        assertEquals(0, result.size(), "The copy should be empty now");

        List<Task> freshResult = taskService.findAll();
        assertEquals(2, freshResult.size(), "The internal store should still have 2 tasks");
    }

    @Test
    void update_ExistingIdShouldUpdateTitleAndDone() {
        Task original = taskService.create(new Task("Original Title"));
        Task updatePayload = new Task("Updated Title");
        updatePayload.setDone(true);

        Task result = taskService.update(original.getId(), updatePayload);

        assertEquals("Updated Title", result.getTitle());
        assertTrue(result.isDone());
    }

    @Test
    void update_ShouldPreserveOriginalId() {
        Task original = taskService.create(new Task("Original"));
        Long originalId = original.getId();

        // Even if the update payload has a different ID, it should be ignored
        Task updatePayload = new Task(999L, "Updated", true);

        Task result = taskService.update(originalId, updatePayload);

        assertEquals(originalId, result.getId(), "ID must not change during update");
    }

    @Test
    void update_NonExistentIdShouldThrowTaskNotFoundException() {
        Task updatePayload = new Task("Does not matter");

        TaskNotFoundException exception = assertThrows(
                TaskNotFoundException.class,
                () -> taskService.update(999L, updatePayload)
        );
        assertTrue(exception.getMessage().contains("999"),
                "Exception message should contain the missing ID");
    }

    @Test
    void update_BlankTitleShouldThrowIllegalArgumentException() {
        Task original = taskService.create(new Task("Original"));
        Task updatePayload = new Task("   "); // Blank title

        assertThrows(
                IllegalArgumentException.class,
                () -> taskService.update(original.getId(), updatePayload)
        );
    }

    @Test
    void delete_ExistingIdShouldReturnTrueAndRemoveTask() {
        Task task = taskService.create(new Task("To be deleted"));
        Long id = task.getId();

        boolean result = taskService.delete(id);

        assertTrue(result, "Delete should return true for existing ID");
        assertTrue(taskService.findById(id).isEmpty(), "Task should no longer be found");
    }

    @Test
    void delete_MissingIdShouldReturnFalse() {
        boolean result = taskService.delete(999L);

        assertFalse(result, "Delete should return false for missing ID");
    }

    @Test
    void delete_NullIdShouldReturnFalse() {
        boolean result = taskService.delete(null);

        assertFalse(result, "Delete should return false for null ID");
    }
}
