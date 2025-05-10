package main.lab1.services;

import main.lab1.model.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {
    private final List<Notification> notifications = new ArrayList<>();

    public List<Notification> getAllNotifications() {
        return notifications;
    }

    public List<Notification> getNotificationsByUserId(long userId) {
        return notifications.stream().filter(notif -> notif.getUserId() == userId).toList();
    }

    public List<Notification> getNotificationsByTaskId(long taskId) {
        return notifications.stream().filter(notif -> notif.getTaskId() == taskId).toList();
    }

    public void createNotification(Notification notification) {
        notifications.add(notification);
    }

}
