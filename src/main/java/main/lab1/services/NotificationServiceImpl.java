package main.lab1.services;

import main.lab1.model.Notification;
import main.lab1.repos.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {
    //private final List<Notification> notifications = new ArrayList<>();

    final private NotificationRepository notificationRepository;

    private final TaskService taskService;

    private final UserService userService;

    public NotificationServiceImpl(NotificationRepository notificationRepository, TaskService taskService, UserService userService) {
        this.notificationRepository = notificationRepository;
        this.taskService = taskService;
        this.userService = userService;
    }


    public List<Notification> getAllNotifications() {
        return notificationRepository.findAll();
    }

    public List<Notification> getNotificationsByUserId(long userId) {
        return notificationRepository.findByUserId(userId);
    }

    public List<Notification> getNotificationsByTaskId(long taskId) {
        return notificationRepository.findByTaskId(taskId);
    }

    public Notification createNotification(Notification notification) {
        return notificationRepository.save(notification);
    }

}
