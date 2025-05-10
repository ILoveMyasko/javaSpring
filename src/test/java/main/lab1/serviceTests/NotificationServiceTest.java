package main.lab1.serviceTests;

import main.lab1.controllers.NotificationController;
import main.lab1.model.Notification;

import main.lab1.services.NotificationServiceImpl;
import main.lab1.services.TaskService;
import main.lab1.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

//unit tests
@ExtendWith(MockitoExtension.class)
public class NotificationServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private TaskService taskService;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    @Test
    void createNotification_ShouldCreateOneNotification() {
        Notification notification = new Notification(1,1,1,"Notification");
        notificationService.createNotification(notification);
        List<Notification> notifications = notificationService.getAllNotifications();

        assertAll(
                ()->assertEquals(1,notifications.size()),
                ()->assertTrue(notifications.contains(notification))
        );
    }

    @Test
    void getNotificationByUserId_WithExistingUserId_ShouldReturnListOfUsersNotifications() {
        int userId = 1;
        Notification notification1User1 = new Notification(1,userId,1,"Notification1");
        Notification notification2User1 = new Notification(2,userId,2,"Notification2");
        Notification notification1User2 = new Notification(3,userId+1,3,"Notification3");
        notificationService.createNotification(notification1User1);
        notificationService.createNotification(notification2User1);
        notificationService.createNotification(notification1User2);
        List<Notification> userNotifications = notificationService.getNotificationsByUserId(userId);
        assertAll(
                ()->assertEquals(2,userNotifications.size()),
                ()->assertTrue(userNotifications.contains(notification1User1)),
                ()->assertTrue(userNotifications.contains(notification2User1))
        );

    }
    @Test
    void getNotificationByUserId_WithNonExistentId_ShouldReturnEmptyList() {
        int userId = 1;
        Notification notification1User1 = new Notification(1,userId,1,"Notification1");
        Notification notification2User1 = new Notification(2,userId,2,"Notification2");
        Notification notification1User2 = new Notification(3,userId+1,3,"Notification3");
        notificationService.createNotification(notification1User1);
        notificationService.createNotification(notification2User1);
        notificationService.createNotification(notification1User2);
        List<Notification> userNotifications = notificationService.getNotificationsByUserId(userId+2);
        assertEquals(0,userNotifications.size());
    }

    @Test
    void getAllNotifications_WithNoNotifications_ShouldReturnEmptyList(){
        List<Notification> userNotifications = notificationService.getAllNotifications();
        assertTrue(userNotifications.isEmpty());
    }
    @Test
    void getAllNotifications_WithMultipleNotificationsWithDifferentUserAndTaskIds_ShouldReturnAllNotifications(){
        int userId = 1;
        Notification notification1User1 = new Notification(1,userId,1,"Notification1");
        Notification notification2User1 = new Notification(2,userId,2,"Notification2");
        Notification notification1User2 = new Notification(3,userId+1,3,"Notification3");
        notificationService.createNotification(notification1User1);
        notificationService.createNotification(notification2User1);
        notificationService.createNotification(notification1User2);
        List<Notification> userNotifications = notificationService.getAllNotifications();
        assertAll(
                ()->assertEquals(3,userNotifications.size()),
                ()->assertTrue(userNotifications.contains(notification1User1)),
                ()->assertTrue(userNotifications.contains(notification2User1)),
                ()->assertTrue(userNotifications.contains(notification1User2))
        );
    }

    @Test
    void getNotificationsByTaskId_WithExistingTaskId_ShouldReturnListOfAllTaskNotifications() {
        int taskId = 1;
        Notification notification1User1Task1 = new Notification(1,1,taskId,"Notification1");
        Notification notification2User1Task1 = new Notification(2,1,taskId,"Notification2");
        Notification notification1User2Task1 = new Notification(3,2,taskId,"Notification3");
        Notification notification1User1Task2 = new Notification(4,1,taskId+1,"Notification3");
        notificationService.createNotification(notification1User1Task1);
        notificationService.createNotification(notification2User1Task1);
        notificationService.createNotification(notification1User2Task1);
        notificationService.createNotification(notification1User1Task2);
        List<Notification> userNotifications = notificationService.getNotificationsByTaskId(taskId);
        assertAll(
                ()->assertEquals(3,userNotifications.size()),
                ()->assertTrue(userNotifications.contains(notification1User1Task1)),
                ()->assertTrue(userNotifications.contains(notification2User1Task1)),
                ()->assertTrue(userNotifications.contains(notification1User2Task1))
        );
    }

    @Test
    void getNotificationsByTaskId_WithNonExistentTaskId_ShouldReturnEmptyList() {
        int taskId = 1;
        Notification notification1User1Task1 = new Notification(1,1,taskId,"Notification1");
        Notification notification1User1Task2 = new Notification(2,1,taskId+1,"Notification3");
        notificationService.createNotification(notification1User1Task1);
        notificationService.createNotification(notification1User1Task2);//shouldn't be listed
        List<Notification> userNotifications = notificationService.getNotificationsByTaskId(taskId+2);
        assertEquals(0,userNotifications.size());

    }

}
