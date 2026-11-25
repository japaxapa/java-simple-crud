package com.example.crud.service;

import com.example.crud.exception.TaskNotFoundException;
import com.example.crud.model.Task;
import com.example.crud.repository.TaskRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("TaskService")
public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    @Nested
    @DisplayName("create()")
    class CreateTests {

        @Test
        @DisplayName("should save the task and return it")
        void create_ShouldSaveTask() {
            Task newTask = new Task("Learn Mockito");
            Task createdTask = new Task(1L, "Learn Mockito", false);

            when(taskRepository.save(any(Task.class))).thenReturn(createdTask);

            Task result = taskService.create(newTask);

            assertEquals(1L, result.getId());
            assertEquals("Learn Mockito", result.getTitle());
            verify(taskRepository, times(1)).save(newTask);
        }

        @Test
        @DisplayName("should reject null or blank titles")
        void create_InvalidTitle_ShouldThrowException() {
            // Null task
            assertThrows(IllegalArgumentException.class, () -> taskService.create(null));

            // Null title
            assertThrows(IllegalArgumentException.class, () -> taskService.create(new Task((String) null)));

            // Empty title
            assertThrows(IllegalArgumentException.class, () -> taskService.create(new Task("")));

            // Whitespace title
            assertThrows(IllegalArgumentException.class, () -> taskService.create(new Task("   ")));

            verify(taskRepository, never()).save(any());
        }

        @Test
        @DisplayName("should reject a null task")
        void create_NullTaskShouldThrowException() {
            assertThrows(IllegalArgumentException.class, () -> taskService.create(null));
            verify(taskRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("findById()")
    class FindByIdTests {

        @Test
        @DisplayName("should return the task from the repository")
        void findById_ExistingIdShouldReturnTask() {
            Task task = new Task(1L, "Find me", false);
            when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

            Optional<Task> result = taskService.findById(1L);

            assertTrue(result.isPresent());
            assertEquals(task, result.get());
        }

        @Test
        @DisplayName("should return empty for a missing ID")
        void findById_MissingIdShouldReturnEmpty() {
            Long missingId = 99L;
            when(taskRepository.findById(missingId)).thenReturn(Optional.empty());

            Optional<Task> result = taskService.findById(missingId);

            assertTrue(result.isEmpty(), "Optional should be empty for a missing ID");
            verify(taskRepository, times(1)).findById(missingId);
        }


        @Test
        @DisplayName("should return empty for a null ID")
        void findById_NullIdShouldReturnEmpty() {
            Optional<Task> result = taskService.findById(null);
            assertTrue(result.isEmpty());
            verify(taskRepository, never()).findById(any());
        }
    }

    @Nested
    @DisplayName("findAll()")
    class FindAllTests {

        @Test
        @DisplayName("should return an empty list when store is empty")
        void findAll_EmptyStoreShouldReturnEmptyList() {
            when(taskRepository.findAll()).thenReturn(Collections.emptyList());

            List<Task> result = taskService.findAll();

            assertNotNull(result, "List should not be null");
            assertTrue(result.isEmpty(), "List should be empty");
            verify(taskRepository, times(1)).findAll();
        }

        @Test
        @DisplayName("should return all tasks from the repository")
        void findAll_ShouldReturnAllTasks() {
            Task task1 = new Task(1L, "Task 1", false);
            Task task2 = new Task(2L, "Task 2", true);
            Task task3 = new Task(3L, "Task 3", true);
            List<Task> expectedTasks = List.of(task1, task2, task3);

            when(taskRepository.findAll()).thenReturn(expectedTasks);

            List<Task> result = taskService.findAll();

            assertEquals(3, result.size());
            assertEquals(expectedTasks, result);
            verify(taskRepository, times(1)).findAll();
        }
    }


    @Nested
    @DisplayName("update()")
    class UpdateTests {

        @Test
        @DisplayName("should update the title and save to repository")
        void update_ExistingIdShouldUpdateAndSave() {
            Task existingTask = new Task(1L, "Old Title", false);
            Task updatePayload = new Task("New Title");
            updatePayload.setDone(true);

            when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));
            when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

            Task result = taskService.update(1L, updatePayload);

            assertEquals("New Title", result.getTitle());
            assertTrue(result.isDone());
            verify(taskRepository, times(1)).save(existingTask);
        }

        @Test
        @DisplayName("should preserve the original ID")
        void update_ShouldPreserveOriginalId() {
            Task existingTask = new Task(1L, "Original", false);
            Task updatePayload = new Task(999L, "Updated", true);

            when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));
            when(taskRepository.save(any(Task.class))).thenAnswer(i -> i.getArgument(0));

            Task result = taskService.update(1L, updatePayload);

            assertEquals(1L, result.getId(), "ID must not change during update");
            verify(taskRepository, times(1)).save(existingTask);
        }

        @Test
        @DisplayName("should throw TaskNotFoundException if ID is missing")
        void update_NonExistentIdShouldThrowException() {
            when(taskRepository.findById(99L)).thenReturn(Optional.empty());

            assertThrows(TaskNotFoundException.class,
                    () -> taskService.update(99L, new Task("Does not matter")));

            verify(taskRepository, never()).save(any());
        }

        @Test
        @DisplayName("should reject a blank title")
        void update_BlankTitleShouldThrowIllegalArgumentException() {
            Task existingTask = new Task(1L, "Original", false);
            Task updatePayload = new Task("   "); // Blank title

            when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));

            assertThrows(
                    IllegalArgumentException.class,
                    () -> taskService.update(1L, updatePayload)
            );

            verify(taskRepository, never()).save(any(Task.class));
        }
    }

    @Nested
    @DisplayName("delete()")
    class DeleteTests {

        @Test
        @DisplayName("should return true and delete when ID exists")
        void delete_ExistingId_ShouldReturnTrue() {
            Long id = 1L;
            when(taskRepository.existsById(id)).thenReturn(true);

            boolean result = taskService.delete(id);

            assertTrue(result);

            verify(taskRepository, times(1)).deleteById(id);
            verify(taskRepository, never()).findById(any());
        }

        @Test
        @DisplayName("should return false when ID is missing")
        void delete_MissingId_ShouldReturnFalse() {
            when(taskRepository.existsById(99L)).thenReturn(false);

            boolean result = taskService.delete(99L);

            assertFalse(result);
            verify(taskRepository, never()).deleteById(any());
        }

        @Test
        @DisplayName("should return false for a null ID")
        void delete_NullIdShouldReturnFalse() {
            boolean result = taskService.delete(null);

            assertFalse(result, "Delete should return false for null ID");

            verify(taskRepository, never()).existsById(any());
            verify(taskRepository, never()).deleteById(any());
        }
    }

}
