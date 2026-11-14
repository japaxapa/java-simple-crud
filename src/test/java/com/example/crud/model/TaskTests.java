package com.example.crud.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    void testGettersAndConstructors() {
        // Test the constructor for new tasks (id should be null)
        Task newTask = new Task("Learn Java");
        assertNull(newTask.getId(), "ID should be null for a new task");
        assertEquals("Learn Java", newTask.getTitle());
        assertFalse(newTask.isDone(), "Done should be false by default");

        // Test setters
        newTask.setId(1L);
        newTask.setTitle("Learn Spring");
        newTask.setDone(true);

        assertEquals(1L, newTask.getId());
        assertEquals("Learn Spring", newTask.getTitle());
        assertTrue(newTask.isDone());
    }

    @Test
    void testEqualsAndHashCode_BasedOnId() {
        Task task1 = new Task(1L, "Task A", false);
        Task task2 = new Task(1L, "Task B", true); // Same ID, different data
        Task task3 = new Task(2L, "Task A", false); // Different ID

        // 1. Same ID = should be equal (even if title/done are different)
        assertEquals(task1, task2, "Tasks with the same ID should be equal");
        assertEquals(task1.hashCode(), task2.hashCode(), "HashCodes should match for same ID");

        // 2. Different ID = should NOT be equal
        assertNotEquals(task1, task3, "Tasks with different IDs should not be equal");
    }

    @Test
    void testEquals_EdgeCases() {
        Task task = new Task(1L, "Task", false);

        // Compare with null
        assertNotEquals(null, task);

        // Compare with a different class
        assertNotEquals((Object) "Not a task", task);

        // Compare with itself
        Task taskCopy = new Task(1L, "Task", false);
        assertEquals(task, taskCopy);
    }
}