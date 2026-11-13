
package com.devops.todo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TodoAppApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void healthEndpointShouldReturnUp() throws Exception {
        mockMvc.perform(get("/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"));
    }

    @Test
    void getTodosShouldReturnInitialTodos() throws Exception {
        mockMvc.perform(get("/todos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title")
                        .value("Setup DevOps pipeline"))
                .andExpect(jsonPath("$[1].title")
                        .value("Deploy to Kubernetes"));
    }

    @Test
    void createTodoShouldReturnCreatedTodo() throws Exception {
        mockMvc.perform(post("/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"Automated test todo\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title")
                        .value("Automated test todo"))
                .andExpect(jsonPath("$.completed").value(false));
    }

    @Test
    void createTodoWithoutTitleShouldReturnBadRequest()
            throws Exception {
        mockMvc.perform(post("/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateTodoShouldChangeItsValues() throws Exception {
        mockMvc.perform(put("/todos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"Updated test todo\","
                        + "\"completed\":false}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title")
                        .value("Updated test todo"))
                .andExpect(jsonPath("$.completed").value(false));
    }

    @Test
    void updateMissingTodoShouldReturnNotFound()
            throws Exception {
        mockMvc.perform(put("/todos/9999")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"Missing todo\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteTodoShouldRemoveIt() throws Exception {
        mockMvc.perform(delete("/todos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message")
                        .value("Todo deleted successfully"));

        mockMvc.perform(get("/todos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.id == 1)]").isEmpty());
    }
}
