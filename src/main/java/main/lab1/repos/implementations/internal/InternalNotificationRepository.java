package main.lab1.repos.implementations.internal;

import main.lab1.repos.NotificationRepository;
import main.lab1.model.Notification;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Repository
@Profile("internal")
public class InternalNotificationRepository implements NotificationRepository {

    private final List<Notification> notifications = new ArrayList<>();
    private static final AtomicLong idCounter = new AtomicLong();

    @Override
    public List<Notification> findAll() {
        return notifications;
    }

    @Override
    public List<Notification> findByUserId(long userId) {
        return notifications.stream().filter(notification -> notification.getUserId() == userId).toList();
    }

    @Override
    public List<Notification> findByTaskId(long taskId) {
        return notifications.stream().filter(notification -> notification.getTaskId() == taskId).toList();
    }

    @Override
    public Notification save(Notification newNotification) {
        newNotification.setNotificationId(idCounter.incrementAndGet());
        notifications.add(newNotification);
        return newNotification;
    }
}
