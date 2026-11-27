package com.example.crud.controller;

import com.example.crud.TestApplication;
import com.example.crud.model.Task;
import com.example.crud.service.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TaskController.class)
@ContextConfiguration(classes = TestApplication.class)
public class TaskControllerTest {

    @Autowired MockMvc mockMvc;
    @MockitoBean TaskService taskService;

    @Test
    void getMissingTaskReturns404() throws Exception {
        when(taskService.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/tasks/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createReturns201() throws Exception {
        Task saved = new Task();
        saved.setId(1L);
        saved.setTitle("x");
        when(taskService.create(any())).thenReturn(saved);

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"x\"}"))
                .andExpect(status().isCreated());
    }
}