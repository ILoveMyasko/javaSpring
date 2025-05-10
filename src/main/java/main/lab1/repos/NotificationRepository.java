package main.lab1.repos;

import main.lab1.model.Notification;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository {

    List<Notification> findAll();
    List<Notification> findByUserId(long id);
    List<Notification> findByTaskId(long id);
    Notification save(Notification newNotification);
}
