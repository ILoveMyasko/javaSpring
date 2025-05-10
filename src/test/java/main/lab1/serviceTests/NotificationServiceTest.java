package main.lab1.serviceTests;
import main.lab1.model.Notification;
import main.lab1.repos.NotificationRepository;
import main.lab1.services.implementation.NotificationServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

//unit tests
@ExtendWith(MockitoExtension.class)
public class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    private Notification notificationToSave;
    private Notification notificationUser1Task1;
    private Notification notificationUser1Task2;
    private Notification notificationUser2Task1;


    @BeforeEach
    void setUp() {
        notificationToSave = new Notification(1,1,1,"Just Notification");
        notificationUser1Task1 = new Notification(1,1,1,"Notification User1Task1");
        notificationUser1Task2 = new Notification(2,1,2,"Notification User1Task2");
        notificationUser2Task1 = new Notification(3,2,1,"Notification User2Task1");
    }

    @Test
    void createNotification_ShouldCreateOneNotification() {
        when(notificationRepository.findAll()).thenReturn(List.of());
        List<Notification> allNotificationsBeforeAddingOne = notificationService.getAllNotifications();

        when(notificationRepository.save(notificationToSave)).thenReturn(notificationToSave);
        Notification createdNotification = notificationService.createNotification(notificationToSave);

        when(notificationRepository.findAll()).thenReturn(List.of(notificationToSave));

        List<Notification> notificationsAfterAddingOne = notificationService.getAllNotifications();
        assertAll(
                ()->assertEquals(0, allNotificationsBeforeAddingOne.size()),
                ()->assertEquals(1, notificationsAfterAddingOne.size()),
                ()->assertEquals(notificationToSave,createdNotification),
                ()->assertTrue(notificationsAfterAddingOne.contains(notificationToSave))
        );
        verify(notificationRepository).save(notificationToSave);
    }

    @Test
    void getNotificationByUserId_WithExistingUserId_ShouldReturnListOfUsersNotifications() {

        when(notificationRepository.findByUserId(1L)).thenReturn(List.of(notificationUser1Task1,notificationUser1Task2));

        List<Notification> userNotifications = notificationService.getNotificationsByUserId(1L);

        assertAll(
                ()->assertEquals(2,userNotifications.size()),
                ()->assertTrue(userNotifications.contains(notificationUser1Task1)),
                ()->assertTrue(userNotifications.contains(notificationUser1Task2))
        );
        verify(notificationRepository).findByUserId(1);
    }
    @Test
    void getNotificationByUserId_WithNonExistentId_ShouldReturnEmptyList() {
        when(notificationRepository.findByUserId(0L)).thenReturn(List.of());

        List<Notification> userNotifications = notificationService.getNotificationsByUserId(0L);
        assertTrue(userNotifications.isEmpty());
        verify(notificationRepository).findByUserId(0L);
    }

    @Test
    void getAllNotifications_WithNoNotifications_ShouldReturnEmptyList(){
        when(notificationRepository.findAll()).thenReturn(List.of());
        List<Notification> allNotifications = notificationService.getAllNotifications();
        assertTrue(allNotifications.isEmpty());
        verify(notificationRepository).findAll();
    }

    @Test
    void getAllNotifications_WithMultipleNotificationsWithDifferentUserAndTaskIds_ShouldReturnAllNotifications(){

        when(notificationRepository.findAll()).thenReturn(List.of(notificationUser1Task1,notificationUser1Task2,notificationUser2Task1));
        List<Notification> userNotifications = notificationService.getAllNotifications();

        assertAll(
                ()->assertEquals(3,userNotifications.size()),
                ()->assertTrue(userNotifications.contains(notificationUser1Task1)),
                ()->assertTrue(userNotifications.contains(notificationUser1Task2)),
                ()->assertTrue(userNotifications.contains(notificationUser2Task1))
        );
        verify(notificationRepository).findAll();
    }

    @Test
    void getNotificationsByTaskId_WithExistingTaskId_ShouldReturnListOfAllTaskNotifications() {

        when(notificationRepository.findByTaskId(1L)).thenReturn(List.of(notificationUser1Task1,notificationUser2Task1));
        List<Notification> userNotifications = notificationService.getNotificationsByTaskId(1L);
        assertAll(
                ()->assertEquals(2,userNotifications.size()),
                ()->assertTrue(userNotifications.contains(notificationUser1Task1)),
                ()->assertTrue(userNotifications.contains(notificationUser2Task1))
        );
        verify(notificationRepository).findByTaskId(1L);
    }

    @Test
    void getNotificationsByTaskId_WithNonExistentTaskId_ShouldReturnEmptyList() {
        when(notificationRepository.findByTaskId(0L)).thenReturn(List.of());

        List<Notification> userNotifications = notificationService.getNotificationsByTaskId(0L);
        assertTrue(userNotifications.isEmpty());
        verify(notificationRepository).findByTaskId(0L);

    }

}
