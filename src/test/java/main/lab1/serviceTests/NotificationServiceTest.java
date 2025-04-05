package main.lab1.serviceTests;

import main.lab1.model.Notification;

import main.lab1.repos.NotificationRepository;

import main.lab1.services.NotificationServiceImpl;
import main.lab1.services.TaskService;
import main.lab1.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

//unit tests
@ExtendWith(MockitoExtension.class)
public class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;
    
    @Mock
    private UserService userService;

    @Mock
    private TaskService taskService;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    @Test
    void createNotification_ShouldCreateOneNotification() {
        Notification notification = new Notification(1,1,1,"Notification");
        when(notificationRepository.findAll()).thenReturn(List.of());
        List<Notification> allNotificationsBeforeAddingOne = notificationService.getAllNotifications();

        when(notificationRepository.save(notification)).thenReturn(notification);
        Notification createdNotification = notificationRepository.save(notification);
//
        when(notificationRepository.findAll()).thenReturn(List.of(notification));
        List<Notification> notificationsAfterAddingOne = notificationService.getAllNotifications();
        assertAll(
                ()->assertEquals(0, allNotificationsBeforeAddingOne.size()),
                ()->assertEquals(1, notificationsAfterAddingOne.size()),
                ()->assertEquals(notification,createdNotification),
                ()->assertTrue(notificationsAfterAddingOne.contains(notification))
        );
    }

    @Test
    void getNotificationByUserId_WithExistingUserId_ShouldReturnListOfUsersNotifications() {
        long userId = 1;
        Notification notification1User1 = new Notification(1,userId,1,"Notification1");
        Notification notification2User1 = new Notification(2,userId,2,"Notification2");
        Notification notification1User2 = new Notification(3,userId+1,3,"Notification3");

        when(notificationRepository.findByUserId(userId)).thenReturn(List.of(notification1User1,notification2User1));

        List<Notification> userNotifications = notificationService.getNotificationsByUserId(userId);

        assertAll(
                ()->assertEquals(2,userNotifications.size()),
                ()->assertTrue(userNotifications.contains(notification1User1)),
                ()->assertTrue(userNotifications.contains(notification2User1))
        );

    }
    @Test
    void getNotificationByUserId_WithNonExistentId_ShouldReturnEmptyList() {
        long invalidUserId = 1;
        when(notificationRepository.findByUserId(invalidUserId)).thenReturn(List.of());
        List<Notification> userNotifications = notificationService.getNotificationsByUserId(invalidUserId);
        assertTrue(userNotifications.isEmpty());
    }

    @Test
    void getAllNotifications_WithNoNotifications_ShouldReturnEmptyList(){
        when(notificationRepository.findAll()).thenReturn(List.of());
        List<Notification> userNotifications = notificationService.getAllNotifications();
        assertTrue(userNotifications.isEmpty());
    }
    @Test
    void getAllNotifications_WithMultipleNotificationsWithDifferentUserAndTaskIds_ShouldReturnAllNotifications(){
        long userId = 1;
        Notification notification1User1 = new Notification(1,userId,1,"Notification1");
        Notification notification2User1 = new Notification(2,userId,2,"Notification2");
        Notification notification1User2 = new Notification(3,userId+1,3,"Notification3");

        when(notificationRepository.findAll()).thenReturn(List.of(notification1User1,notification2User1,notification1User2));
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
        long taskId = 1;
        Notification notification1User1Task1 = new Notification(1,1,taskId,"Notification1");
        Notification notification2User1Task1 = new Notification(2,1,taskId,"Notification2");
        Notification notification1User2Task1 = new Notification(3,2,taskId,"Notification3");
        //Notification notification1User1Task2 = new Notification(4,1,taskId+1,"Notification3");
        when(notificationRepository.findByTaskId(taskId)).thenReturn(List.of(notification1User1Task1,notification2User1Task1,notification1User2Task1));
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
        long invalidTaskId = 1;
        Notification notification1User1Task1 = new Notification(1,1,1,"Notification1");
        Notification notification1User1Task2 = new Notification(2,1,2,"Notification3");
        when(notificationRepository.findByTaskId(invalidTaskId)).thenReturn(List.of());

        List<Notification> userNotifications = notificationService.getNotificationsByTaskId(invalidTaskId);
        assertTrue(userNotifications.isEmpty());

    }

}
