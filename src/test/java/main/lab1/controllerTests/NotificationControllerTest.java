package main.lab1.controllerTests;

import main.lab1.controllers.NotificationController;
import main.lab1.model.Notification;
import main.lab1.services.NotificationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Arrays;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NotificationControllerTest {

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private NotificationController notificationController;

    @Test
    void getAllNotifications_WithMultipleNotifications_ReturnsEntityWithListOfAllNotifications() {
        List<Notification> mockNotifications = Arrays.asList(
                new Notification(1,1, 1, "Notification1"),
                new Notification(2,1, 2, "Notification2"),
                new Notification(3,2, 3, "Notification3")
        );

        when(notificationService.getAllNotifications()).thenReturn(mockNotifications);
        ResponseEntity<List<Notification>> response = notificationController.getAllNotifications();

        assertThat(response.getBody())
                .isNotNull()
                .isNotEmpty()
                .hasSize(mockNotifications.size());  //what if actually something changes mockNotifs size?
        assertEquals(HttpStatus.OK, response.getStatusCode());

        verify(notificationService, times(1)).getAllNotifications();
    }

    @Test
    void getAllNotifications_NoNotification_ReturnsEntityWithEmptyList() {
        when(notificationService.getAllNotifications()).thenReturn(Collections.emptyList());
        ResponseEntity<List<Notification>> response = notificationController.getAllNotifications();

        assertThat(response.getBody())
                .isNotNull()
                .isEmpty();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(notificationService, times(1)).getAllNotifications();

    }
    @Test
    void getNotificationsByUserId_ForExistingUser_ReturnsEntityWithListOfAllUsersNotifications() {
        int userId = 1;

        Notification notification1User1 = new Notification(1,1, 1, "Notification1");
        Notification notification2User1 = new Notification(2,1, 2, "Notification2");
        Notification notification1User2 = new Notification(3,2, 3, "Notification3");
        List<Notification> mockNotifications = Arrays.asList(
                notification1User1,
                notification2User1,
                notification1User2
        );
        when(notificationService.getNotificationsByUserId(userId)).thenReturn(
                Arrays.asList(notification1User1,  notification2User1)
        );

        ResponseEntity<List<Notification>> response = notificationController.getNotificationsByUserId(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThat(response.getBody())
                .isNotNull()
                .isNotEmpty()
                .hasSize(2)
                .containsExactlyInAnyOrder(notification1User1, notification2User1)
                .doesNotContain(notification1User2);

        //?
        verify(notificationService, times(1)).getNotificationsByUserId(userId);
    }
    @Test
    void getNotificationsByUserId_ForNonExistentUser_ReturnsEntityWithEmptyList() {
        int userId = 3;

        when(notificationService.getNotificationsByUserId(userId)).thenReturn(Collections.emptyList());
        ResponseEntity<List<Notification>> response = notificationController.getNotificationsByUserId(userId);

        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertThat(response.getBody())
                .isNotNull()
                .isEmpty();
        verify(notificationService,times(1)).getNotificationsByUserId(userId);
    }
    @Test
    void getNotificationsByTaskId_WithExistingTaskId_ReturnsEntityWithListOfTasksNotifications() {
        int taskId = 2;
        Notification notification1Task1 = new Notification(1,1,1,"Notification1");
        Notification notification2Task2 = new Notification(2,1,2,"Notification2");
        Notification notification1Task2 = new Notification(3,2,2,"Notification3");

        when(notificationService.getNotificationsByTaskId(taskId)).thenReturn(
                Arrays.asList(notification1Task2,  notification2Task2)
        );
        ResponseEntity<List<Notification>> response = notificationController.getNotificationsByTaskId(taskId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThat(response.getBody())
                .isNotNull()
                .isNotEmpty()
                .hasSize(2)
                .containsExactlyInAnyOrder(notification2Task2, notification1Task2)
                .doesNotContain(notification1Task1);

        verify(notificationService, times(1)).getNotificationsByTaskId(taskId);

    }
    @Test
    void getNotificationsByTaskId_WithNonExistentTaskId_ThrowsTaskNotFoundException(){
        int taskId = 3;

        when(notificationService.getNotificationsByTaskId(taskId)).thenReturn(Collections.emptyList());
        ResponseEntity<List<Notification>> response = notificationController.getNotificationsByTaskId(taskId);

        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertThat(response.getBody())
                .isNotNull()
                .isEmpty();
        verify(notificationService,times(1)).getNotificationsByTaskId(taskId);
    }
}
