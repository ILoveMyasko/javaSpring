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

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserService userService;

    public NotificationServiceImpl(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }


    public List<Notification> getAllNotifications() {
        return notificationRepository.findAll();
    }

    public List<Notification> getNotificationsByUserId(long userId) {
        userService.getUserById(userId);
        return notificationRepository.findByUserId(userId);
    }

    public List<Notification> getNotificationsByTaskId(long taskId) {
        taskService.getTaskById(taskId);
        return notificationRepository.findByTaskId(taskId);
    }

    public void createNotification(Notification notification) {
        userService.getUserById(notification.getUserId());
        taskService.getTaskById(notification.getTaskId());
        notificationRepository.save(notification);
    }

}
