package main.lab1.services;

import main.lab1.model.Notification;
import main.lab1.repos.NotificationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    final private NotificationRepository notificationRepository;

    public NotificationServiceImpl(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Transactional(readOnly = true)
    public List<Notification> getAllNotifications() {
        return notificationRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Notification> getNotificationsByUserId(long userId) {
        return notificationRepository.findByUserId(userId);
    }

    @Transactional(readOnly = true)
    public List<Notification> getNotificationsByTaskId(long taskId) {
        return notificationRepository.findByTaskId(taskId);
    }

    @Transactional
    public Notification createNotification(Notification notification) {
        return notificationRepository.save(notification);
    }

}
