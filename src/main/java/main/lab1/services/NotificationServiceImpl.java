package main.lab1.services;

import main.lab1.model.Notification;
import main.lab1.model.Task;
import main.lab1.repos.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
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
        userService.getUserById(userId);
        return notificationRepository.findByUserId(userId);
    }

    public List<Notification> getNotificationsByTaskId(long taskId) {
        taskService.getTaskById(taskId);
        return notificationRepository.findByTaskId(taskId);
    }
/* //
    public Notification createNotification(Notification notification) { //remove after adding kafka?
        userService.getUserById(notification.getUserId());
        taskService.getTaskById(notification.getTaskId());
        return notificationRepository.save(notification);
    }

 */
    //now we have this async handler instead of manual task creations
    @KafkaListener(topics = "task-events")
    public void handleTaskEvent(Task event) {
        Notification notification = new Notification(); // deepseek says notificationId will be fine
        notification.setTaskId(event.getTaskId());
        notification.setUserId(event.getUserId());
        notification.setText("Task " + event.getTaskId()+" created");
        notificationRepository.save(notification);
    }
}
