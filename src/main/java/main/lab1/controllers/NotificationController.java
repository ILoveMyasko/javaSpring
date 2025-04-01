package main.lab1.controllers;

import jakarta.validation.Valid;
import main.lab1.exceptions.TaskNotFoundException;
import main.lab1.exceptions.UserNotFoundException;
import main.lab1.model.Notification;
import main.lab1.services.NotificationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }
    
    @GetMapping
    public ResponseEntity<List<Notification>> getAllNotifications(){
        return ResponseEntity.ok(notificationService.getAllNotifications());
    }
    @GetMapping("/users/{id}")
    public ResponseEntity<List<Notification>> getNotificationsByUserId(@PathVariable("id") long uId){
        return ResponseEntity.ok(notificationService.getNotificationsByUserId(uId));
    }
    @GetMapping("/tasks/{id}")
    public ResponseEntity<List<Notification>> getNotificationsByTaskId(@PathVariable("id") long uId){
        return ResponseEntity.ok(notificationService.getNotificationsByTaskId(uId));
    }
    //not required but how to create notifs?
    //okay some of them can be created automatically but only some not all.
    @PostMapping
    public void createNotification(@RequestBody @Valid Notification notification) { //request body builds Notification object through json?
        notificationService.createNotification(notification);
    }

    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<String> handleTaskNotFoundException(TaskNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
}
