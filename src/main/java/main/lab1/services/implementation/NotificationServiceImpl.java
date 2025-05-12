package main.lab1.services.implementation;

import main.lab1.kafkaEvents.TaskEvent;
import main.lab1.model.Notification;
import main.lab1.model.Task;
import main.lab1.repos.NotificationRepository;
import main.lab1.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
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

    //now we have this async handler instead of manual task creations
    @KafkaListener(topics = "task-events")
    public void handleTaskEvent(TaskEvent event) {
        switch (event.eventType()){
            case CREATE: {
                Notification notification = new Notification(); // deepseek says notificationId will be fine
                notification.setTaskId(event.taskId());
                notification.setUserId(event.userId());
                notification.setText("Task " + event.taskId()+" created");
                this.createNotification(notification);
                break;
            }
            case UPDATE: {
                Notification notification = new Notification();
                notification.setTaskId(event.taskId());
                notification.setUserId(event.userId());
                notification.setText("Task " + event.taskId()+" completed");
                this.createNotification(notification);
                break;
            }
            case DELETE: {
                break;
            }
            default: {
                System.out.println("No actions implemented for event type: " + event.eventType());
            }
        }

    }
}
