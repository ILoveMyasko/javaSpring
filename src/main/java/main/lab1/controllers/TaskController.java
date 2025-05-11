package main.lab1.controllers;
import jakarta.validation.Valid;
import main.lab1.exceptions.DuplicateResourceException;
import main.lab1.exceptions.ResourceNotFoundException;
import main.lab1.model.Task;
import main.lab1.services.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;

    TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks(){
        return ok(taskService.getAllTasks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable("id") long uId){
        return ok(taskService.getTaskById(uId));
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<List<Task>> getAllTasksByUserId(@PathVariable("id") long uId){
        return ok(taskService.getTasksByUserId(uId));
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody @Valid Task Task) { //request body builds Task object through json?
        return ResponseEntity.ok(taskService.createTask(Task));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Task> deleteTask(@PathVariable("id") long tId){ //okay lets return deleted
        return ok(taskService.deleteTaskById(tId));
    }

    @PatchMapping("/complete/{id}")
    public ResponseEntity<Void> markAsCompleted(@PathVariable long id) {
        taskService.markAsCompleted(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<String> handleDuplicateResourceException(DuplicateResourceException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

}

