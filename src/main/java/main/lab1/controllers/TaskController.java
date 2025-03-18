package main.lab1.controllers;
import jakarta.validation.Valid;
import main.lab1.entities.Task;
import main.lab1.exceptions.TaskAlreadyExistsException;
import main.lab1.exceptions.TaskNotFoundException;
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
    public ResponseEntity<Task> getTaskById(@PathVariable("id") int uId){
        return ResponseEntity.ok(taskService.getTaskById(uId));
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<List<Task>> getAllTasksByUserId(@PathVariable("id") int uId){
        return ResponseEntity.ok(taskService.getTasksByUserId(uId));
    }

    @PostMapping
    public void createTask(@RequestBody @Valid Task Task) { //request body builds Task object through json?
        taskService.createTask(Task);
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable("id") int tId){
        taskService.deleteTaskById(tId);
    }


    @ExceptionHandler(TaskAlreadyExistsException.class)
    public ResponseEntity<String> handleTaskAlreadyExistsException(TaskAlreadyExistsException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<String> handleTaskNotFoundException(TaskNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
}

