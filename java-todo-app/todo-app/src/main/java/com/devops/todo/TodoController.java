
package com.devops.todo;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class TodoController {

    private final Map<Long, Todo> todos = new LinkedHashMap<>();
    private final AtomicLong nextId = new AtomicLong(3);

    public TodoController() {
        todos.put(1L, new Todo(1L, "Setup DevOps pipeline", true));
        todos.put(2L, new Todo(2L, "Deploy to Kubernetes", false));
    }

    @GetMapping("/")
    public Map<String, String> home() {
        return Map.of(
            "message", "Welcome to Java Todo API",
            "endpoints", "/health, /todos"
        );
    }

    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of("status", "UP");
    }

    @GetMapping("/todos")
    public List<Todo> getTodos() {
        return new ArrayList<>(todos.values());
    }

    @PostMapping("/todos")
    public ResponseEntity<?> createTodo(@RequestBody Map<String, Object> body) {
        Object titleValue = body.get("title");

        if (!(titleValue instanceof String) ||
                ((String) titleValue).isBlank()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Title is required"));
        }

        boolean completed = Boolean.TRUE.equals(body.get("completed"));

        long id = nextId.getAndIncrement();
        Todo todo = new Todo(id, (String) titleValue, completed);
        todos.put(id, todo);

        return ResponseEntity.status(HttpStatus.CREATED).body(todo);
    }

    @PutMapping("/todos/{id}")
    public ResponseEntity<?> updateTodo(
            @PathVariable Long id,
            @RequestBody Map<String, Object> body) {

        Todo todo = todos.get(id);

        if (todo == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Todo not found"));
        }

        if (body.containsKey("title")) {
            Object titleValue = body.get("title");

            if (!(titleValue instanceof String) ||
                    ((String) titleValue).isBlank()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Invalid title"));
            }

            todo.setTitle((String) titleValue);
        }

        if (body.containsKey("completed")) {
            Object completedValue = body.get("completed");

            if (!(completedValue instanceof Boolean)) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Invalid completed value"));
            }

            todo.setCompleted((Boolean) completedValue);
        }

        return ResponseEntity.ok(todo);
    }

    @DeleteMapping("/todos/{id}")
    public ResponseEntity<?> deleteTodo(@PathVariable Long id) {
        Todo removed = todos.remove(id);

        if (removed == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Todo not found"));
        }

        return ResponseEntity.ok(
                Map.of("message", "Todo deleted successfully"));
    }

    public static class Todo {
        private Long id;
        private String title;
        private boolean completed;

        public Todo(Long id, String title, boolean completed) {
            this.id = id;
            this.title = title;
            this.completed = completed;
        }

        public Long getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public boolean isCompleted() {
            return completed;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setCompleted(boolean completed) {
            this.completed = completed;
        }
    }
}
