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
    @Transactional
    @KafkaListener(topics = "task-events")
    public void handleTaskEvent(TaskEvent taskEvent) {
        switch (taskEvent.eventType()){
            case CREATE: {
                Notification notification = new Notification();
                notification.setTaskId(taskEvent.taskId());
                notification.setUserId(taskEvent.userId());
                notification.setText("Task " + taskEvent.taskId()+" created");
                notificationRepository.save(notification);
                break;
            }
            case UPDATE: {
                Notification notification = new Notification();
                notification.setTaskId(taskEvent.taskId());
                notification.setUserId(taskEvent.userId());
                notification.setText("Task " + taskEvent.taskId()+" completed");
                notificationRepository.save(notification);
                break;
            }
            case DELETE: {
                notificationRepository.deleteByTaskId(taskEvent.taskId());
                break;
            }
            default: {
                System.out.println("No actions implemented for taskEvent type: " + taskEvent.eventType());
            }
        }

    }
}
