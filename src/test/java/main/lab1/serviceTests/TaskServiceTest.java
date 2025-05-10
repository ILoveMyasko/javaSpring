package main.lab1.serviceTests;

import main.lab1.exceptions.UserNotFoundException;
import main.lab1.model.Task;
import main.lab1.exceptions.DuplicateResourceException;
import main.lab1.exceptions.TaskNotFoundException;
import main.lab1.model.User;
import main.lab1.services.NotificationService;
import main.lab1.services.TaskServiceImpl;
import main.lab1.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

//unit tests
@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {
    @Mock
    private UserService userService;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private TaskServiceImpl taskService;

    @Test
    void createTask_WithNewTaskId_ShouldAddOneTask() {
        long userId = 1;
        User mockUser = new User(userId, "Alex", "alex@ex.com");
        when(userService.getUserById(userId)).thenReturn(mockUser);
        long taskId = 1;
        Task task = new Task(taskId,userId,"Title","Description", ZonedDateTime.now().plusHours(1));
        long tasksCount = taskService.getAllTasks().size();
        taskService.createTask(task);
        assertDoesNotThrow(() -> taskService.getTaskById(taskId));
        assertEquals(tasksCount+1, taskService.getAllTasks().size());
        assertTrue(taskService.getAllTasks().contains(task));
    }
    @Test
    void createTask_ForNonExistentUserId_ShouldThrowUserNotFoundException() {
        int invalidUserId = -1;
        Task invalidTask = new Task(1, invalidUserId, "Title", "Description", ZonedDateTime.now().plusHours(3));
        when(userService.getUserById(invalidUserId))
                .thenThrow(new UserNotFoundException(invalidUserId));

        assertThrows(UserNotFoundException.class, () -> taskService.createTask(invalidTask));
    }
    @Test
    void createTask_WithExistingTaskId_ShouldThrowException() {
        long userId = 1;
        long taskId = 1;
        User mockUser = new User(userId, "Alex", "alex@ex.com");
        when(userService.getUserById(userId)).thenReturn(mockUser);//is it okay to use getUserById to check for existnece? not bool function
        Task task1 = new Task(taskId,userId,"Title","Description", ZonedDateTime.now().plusHours(3));
        Task task2 = new Task(taskId,userId,"Title","Description", ZonedDateTime.now().plusHours(3));

        taskService.createTask(task1);
        assertThrows(
                DuplicateResourceException.class,
                () -> taskService.createTask(task2)
        );
    }
    @Test
    void getTask_WithExistingTaskId_ShouldReturnTask() {
        long userId = 1;
        User mockUser = new User(userId, "Alex", "alex@ex.com");
        when(userService.getUserById(userId)).thenReturn(mockUser);//since
        long taskId = 1;
        Task expectedTask = new Task(taskId,1,"Title","Description", ZonedDateTime.now().plusHours(3));
        taskService.createTask(expectedTask);
        Task actualTask = taskService.getTaskById(taskId);
        assertEquals(expectedTask, actualTask);//ignore that we can throw an exception because this fails the test anyway
    }
    @Test
    void getTask_WithNonExistentId_ShouldThrowException() {
        long userId = 1;
        User mockUser = new User(userId, "Alex", "alex@ex.com");
        when(userService.getUserById(userId)).thenReturn(mockUser);
        long taskId = 1;
        Task task = new Task(taskId,userId,"Title","Description", ZonedDateTime.now().plusHours(3));
        taskService.createTask(task);
        assertThrows(
                TaskNotFoundException.class, //what exception will be thrown
                () ->taskService.getTaskById(taskId-2));
    }



    @Test
    void getAllTasks_WithNoTasks_ShouldReturnEmptyList(){
        assertTrue(taskService.getAllTasks().isEmpty());
    }
    @Test
    void getAllTasks_WithMultipleTasks_ShouldReturnListOfAllTasks(){
        long userId = 1;
        User mockUser1 = new User(userId, "Alex", "alex@ex.com");
        User mockUser2 = new User(userId+1, "Alex", "alex@ex.com");
        when(userService.getUserById(userId)).thenReturn(mockUser1);
        when(userService.getUserById(userId+1)).thenReturn(mockUser2);
        Task task1 = new Task(1,userId,"Title","Description", ZonedDateTime.now().plusHours(3));
        Task task2 = new Task(2,userId+1,"Title","Description", ZonedDateTime.now().plusDays(3).plusYears(1));
        taskService.createTask(task1);
        taskService.createTask(task2);
        List<Task> tasks = taskService.getAllTasks();
        assertAll(//assert there are exactly 2 tasks and both inserted are present
                () -> assertEquals(2, tasks.size()),
                () -> assertTrue(tasks.contains(task1)),
                () -> assertTrue(tasks.contains(task2))
        );


    }

    @Test
    void getTasksByUserId_WithExistingUserId_ShouldReturnListOfAllUsersTasks() {
        long userId = 1;
        User mockUser1 = new User(userId, "Alex", "alex@ex.com");
        User mockUser2 = new User(userId+1, "Alex", "alex@ex.com");
        when(userService.getUserById(userId)).thenReturn(mockUser1);
        when(userService.getUserById(userId+1)).thenReturn(mockUser2);


        Task task1User1 = new Task(1,userId,"Title","Description", ZonedDateTime.now().plusHours(3));
        Task task2User1 = new Task(2,userId,"Title1","Description1", ZonedDateTime.now().plusHours(4));
        Task task1User2 = new Task(3,userId+1,"Title2","Description2", ZonedDateTime.now().plusHours(4));

        taskService.createTask(task1User1);
        taskService.createTask(task1User2);//another user have tasks
        taskService.createTask(task2User1);

        List<Task> userTasks = taskService.getTasksByUserId(userId);
        assertAll(
                ()->assertEquals(2,userTasks.size()),
                ()->assertTrue(userTasks.contains(task1User1)),
                ()->assertTrue(userTasks.contains(task2User1))
        );
    }

    @Test
    void getTasksByUserId_WithNonExistentUserId_ShouldReturnEmptyList() {
        long userId = 1;
        User mockUser1 = new User(userId, "Alex", "alex@ex.com");
        User mockUser2 = new User(userId+1, "Alex", "alex@ex.com");
        when(userService.getUserById(userId)).thenReturn(mockUser1);
        when(userService.getUserById(userId+1)).thenReturn(mockUser2);

        Task task1User1 = new Task(1,userId,"Title","Description", ZonedDateTime.now().plusHours(3));
        Task task1User2 = new Task(2,userId+1,"Title1","Description1", ZonedDateTime.now().plusHours(4));
        taskService.createTask(task1User1);
        taskService.createTask(task1User2);//another user have tasks
        List<Task> userTasks = taskService.getTasksByUserId(userId+2);
        assertEquals(0,userTasks.size());

    }
    @Test
    void deleteTaskById_WithExistingTaskId_ShouldRemoveTask() {
        long userId = 1;
        User mockUser1 = new User(userId, "Alex", "alex@ex.com");
        when(userService.getUserById(userId)).thenReturn(mockUser1);
        long taskId = 1;
        Task taskId1User1 = new Task(taskId,userId ,"Title","Description", ZonedDateTime.now().plusHours(3));
        Task taskId2User2 = new Task(taskId+1,userId ,"Title1","Description1", ZonedDateTime.now().plusHours(4));
        taskService.createTask(taskId1User1);
        taskService.createTask(taskId2User2);
        taskService.deleteTaskById(taskId);
        List<Task> userTasks = taskService.getAllTasks();
        assertAll(
                ()->assertEquals(1,userTasks.size()),
                ()->assertFalse(userTasks.contains(taskId1User1)),
                ()->assertTrue(userTasks.contains(taskId2User2))
                );
    }

    @Test
    void deleteTaskById_WithNonExistentTaskId_ShouldRemoveNothing() {
        long userId = 1;
        User mockUser1 = new User(userId, "Alex", "alex@ex.com");
        when(userService.getUserById(userId)).thenReturn(mockUser1);

        long taskId = 1;
        Task taskId1User1 = new Task(taskId,userId,"Title","Description", ZonedDateTime.now().plusHours(3));
        Task taskId2User2 = new Task(taskId+1,userId,"Title1","Description1", ZonedDateTime.now().plusHours(4));
        taskService.createTask(taskId1User1);
        taskService.createTask(taskId2User2);
        taskService.deleteTaskById(taskId+2);
        List<Task> userTasks = taskService.getAllTasks();
        assertAll(
                ()->assertEquals(2,userTasks.size()),
                ()->assertTrue(userTasks.contains(taskId1User1)),
                ()->assertTrue(userTasks.contains(taskId2User2))
        );
    }
}
