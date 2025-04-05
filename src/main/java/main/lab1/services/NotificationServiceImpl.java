package main.lab1.services;

import jakarta.transaction.Transactional;
import main.lab1.kafkaEvents.TaskEvent;
import main.lab1.model.Notification;
import main.lab1.repos.NotificationRepository;
import main.lab1.repos.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.Async;
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
//
    public Notification createNotification(Notification notification) { //remove after adding kafka?
        userService.getUserById(notification.getUserId());//this is so bad
        taskService.getTaskById(notification.getTaskId());//and this too
        return notificationRepository.save(notification);
    }

    //now we have this async handler instead of manual task creations
    @KafkaListener(topics = "task-events")
    @Transactional //get errors without it
    //@Async // listener's already async, better use concurrency param for @KafkaListener
    public void handleTaskEvent(TaskEvent taskEvent) {
        switch(taskEvent.eventType()){
            case CREATE: {
                Notification notification = new Notification(); // notificationId will be fine
                notification.setTaskId(taskEvent.taskId());
                notification.setUserId(taskEvent.userId());
                notification.setText("Task " + taskEvent.taskId()+" created");
                notificationRepository.save(notification);
                System.out.println("saved notification:"+ notification);
                break;
            }
            case DELETE: {
                notificationRepository.deleteByTaskId(taskEvent.taskId()); //since we delete a task I think we might be sure we don't need notifs.
                break;
            }
            default: {
                System.out.println("No actions implemented for event type: " + taskEvent.eventType());
            }
        }

    }
    //Is it a good idea to create another listener for deleting?

}
