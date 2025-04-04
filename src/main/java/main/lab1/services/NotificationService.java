package main.lab1.services;

import main.lab1.model.Notification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface NotificationService {
    List<Notification> getAllNotifications();

    List<Notification> getNotificationsByUserId(long userId);

    List<Notification> getNotificationsByTaskId(long userId);

    //Notification createNotification(Notification notification); // replaced with kafka async listener
}
