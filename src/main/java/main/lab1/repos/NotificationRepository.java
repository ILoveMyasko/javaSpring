package main.lab1.repos;

import main.lab1.model.Notification;

import java.util.List;

public interface NotificationRepository {

    List<Notification> findAll();
    List<Notification> findByUserId(long id);
    List<Notification> findByTaskId(long id);
    Notification save(Notification newNotification);
}
