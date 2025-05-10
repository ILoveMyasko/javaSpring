package main.lab1.controllers;

import main.lab1.model.Notification;
import main.lab1.services.NotificationService;
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

}
