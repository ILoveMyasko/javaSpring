package main.lab1.services;

import main.lab1.model.Notification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface NotificationService {
    List<Notification> getAllNotifications();

    List<Notification> getNotificationsByUserId(int userId);

    List<Notification> getNotificationsByTaskId(int userId);

    void createNotification(Notification notification);
}
