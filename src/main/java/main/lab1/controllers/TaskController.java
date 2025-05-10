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

@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;

    TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks(){
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable("id") long uId){
        return ResponseEntity.ok(taskService.getTaskById(uId));
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<List<Task>> getAllTasksByUserId(@PathVariable("id") long uId){
        return ResponseEntity.ok(taskService.getTasksByUserId(uId));
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody @Valid Task Task) { //request body builds Task object through json?
        return ResponseEntity.ok(taskService.createTask(Task));
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable("id") long tId){
        taskService.deleteTaskById(tId);
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

