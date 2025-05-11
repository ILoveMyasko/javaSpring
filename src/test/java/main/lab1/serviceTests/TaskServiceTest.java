package main.lab1.serviceTests;

import main.lab1.exceptions.ResourceNotFoundException;
import main.lab1.model.Task;
import main.lab1.repos.TaskRepository;
import main.lab1.services.NotificationService;
import main.lab1.services.implementation.TaskServiceImpl;
import main.lab1.services.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

//unit tests
@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {
    @Mock
    private UserService userService;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private TaskServiceImpl taskService;

    private Task invalidTask;
    private Task taskUser1;
    private Task task2User1;
    private Task taskUser2;

    @BeforeEach
    void setUp()
    {
        taskUser1 = new Task(1, 1, "Title", "Description", ZonedDateTime.now().plusHours(3));
        task2User1 = new Task(2, 1, "Title", "Description", ZonedDateTime.now().plusHours(3));
        taskUser2 = new Task(3, 2, "Title", "Description", ZonedDateTime.now().plusHours(3));

        invalidTask = new Task(-1, -1, "Title", "Description", ZonedDateTime.now().plusHours(3));
    }

    @Test
    void createTask_ForNonExistentUserId_ShouldThrowUserNotFoundException() {
        long invalidUserId = invalidTask.getUserId();
        when(userService.existsByUserId(invalidUserId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> taskService.createTask(invalidTask));

        verify(userService).existsByUserId(invalidUserId);
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    void createTask_ForValidUser_ShouldAddTask()
    {
        when(userService.existsByUserId(taskUser1.getUserId())).thenReturn(true);
        when(taskRepository.save(taskUser1)).thenReturn(taskUser1);

        Task createdTask = taskService.createTask(taskUser1);

        assertNotNull(createdTask);
        assertEquals(taskUser1,createdTask);
        verify(userService).existsByUserId(taskUser1.getUserId());
        verify(taskRepository).save(any(Task.class));

    }

    @Test
    void getTask_WithExistingTaskId_ShouldReturnTask() {

        long taskId = taskUser1.getTaskId();
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(taskUser1));
        
        Task actualTask = taskService.getTaskById(taskId);

        assertNotNull(actualTask);
        assertEquals(taskUser1, actualTask);
        verify(taskRepository).findById(taskId);
    }
    @Test
    void getTask_WithNonExistentId_ShouldThrowException() {

        long invalidTaskId = invalidTask.getTaskId();

        when(taskRepository.findById(invalidTaskId)).thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class, //what exception will be thrown
                () ->taskService.getTaskById(invalidTaskId));
        verify(taskRepository).findById(invalidTaskId);
    }



    @Test
    void getAllTasks_WithNoTasks_ShouldReturnEmptyList(){
        when(taskRepository.findAll()).thenReturn(List.of());

        assertTrue(taskService.getAllTasks().isEmpty());
        verify(taskRepository).findAll();
    }

    @Test
    void getAllTasks_WithMultipleTasks_ShouldReturnListOfAllTasks(){

        List<Task> expectedTasks = List.of(taskUser1, taskUser2, task2User1);
        when(taskRepository.findAll()).thenReturn(expectedTasks);

        List<Task> actualTasks = taskService.getAllTasks();
        assertAll(
                () -> assertEquals(3, actualTasks.size()),
                () -> assertTrue(actualTasks.containsAll(expectedTasks)),
                () -> assertTrue(expectedTasks.containsAll(actualTasks))
        );
        verify(taskRepository).findAll();
    }

    @Test
    void getTasksByUserId_WithExistingUserId_ShouldReturnListOfAllUsersTasks() {
        long userId = taskUser1.getUserId();

        when(taskRepository.findByUserIdAndIsCompletedFalse(userId)).thenReturn(List.of(taskUser1,task2User1));

        List<Task> userTasks = taskService.getTasksByUserId(userId);
        assertAll(
                ()->assertEquals(2,userTasks.size()),
                ()->assertTrue(userTasks.contains(taskUser1)),
                ()->assertTrue(userTasks.contains(task2User1))
        );
        verify(taskRepository).findByUserIdAndIsCompletedFalse(userId);
    }

    @Test
    void getTasksByUserId_WithNonExistentUserId_ShouldReturnEmptyList() {
        long invalidUserId = invalidTask.getUserId();
        when(taskRepository.findByUserIdAndIsCompletedFalse(invalidUserId)).thenReturn(List.of());

        List<Task> userTasks = taskService.getTasksByUserId(invalidUserId);

        assertEquals(0,userTasks.size());
        verify(taskRepository).findByUserIdAndIsCompletedFalse(invalidUserId);
    }

    @Test
    void deleteTaskById_WithExistingTaskId_ShouldRemoveOneTask() {

        long taskIdToDelete = taskUser1.getTaskId();
        when(taskRepository.findById(taskIdToDelete)).thenReturn(Optional.of(taskUser1));
        doNothing().when(taskRepository).deleteById(taskIdToDelete);

        assertDoesNotThrow(()->taskService.deleteTaskById(taskIdToDelete));
        when(taskRepository.findById(taskIdToDelete)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,()->taskService.getTaskById(taskIdToDelete));

        verify(taskRepository).deleteById(taskIdToDelete);
    }

    @Test
    void deleteTaskById_WithNonExistentTaskId_ShouldThrow() {
        long invalidTaskId = invalidTask.getTaskId();
        assertThrows(ResourceNotFoundException.class, ()-> taskService.deleteTaskById(invalidTaskId));
    }

    @Test
    void markAsCompleted_shouldSetCompletedAndSendNotification() {
        Task task = new Task();
        task.setTaskId(1L);
        task.setUserId(100L);
        task.setCompleted(false);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        taskService.markAsCompleted(1L);

        assertTrue(task.isCompleted());
        verify(notificationService).createNotification(
                argThat(notification ->
                        notification.getUserId() == 100L &&
                                notification.getTaskId() == 1L &&
                                notification.getText().equals("Task completed!")
                )
        );
    }
    @Test
    void markAsCompleted_shouldThrowWhenTaskNotFound() {
        when(taskRepository.findById(42L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> taskService.markAsCompleted(42L));
        verifyNoInteractions(notificationService);
    }

}
