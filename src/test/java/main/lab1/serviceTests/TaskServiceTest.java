package main.lab1.serviceTests;

import main.lab1.exceptions.UserNotFoundException;
import main.lab1.model.Task;
import main.lab1.exceptions.TaskAlreadyExistsException;
import main.lab1.exceptions.TaskNotFoundException;
import main.lab1.model.User;
import main.lab1.repos.TaskRepository;
import main.lab1.services.TaskServiceImpl;
import main.lab1.services.UserService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

//unit tests
@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {
    @Mock
    private UserService userService;

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskServiceImpl taskService;

    @Test
    void createTask_WithNewTaskId_ShouldAddOneTask() {
        long userId = 1;
        User mockUser = new User(userId, "Alex", "alex@ex.com");
        long taskId = 1;
        Task newTask = new Task(taskId,userId,"Title","Description", ZonedDateTime.now().plusHours(1));
        when(taskRepository.count()).thenReturn(0L);
        long tasksCount = taskRepository.count();//0

        when(userService.getUserById(userId)).thenReturn(mockUser);
        when(taskRepository.existsById(taskId)).thenReturn(false);

        assertDoesNotThrow(() -> taskService.createTask(newTask));
        taskService.createTask(newTask);
        when(taskRepository.count()).thenReturn(1L);
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(newTask));

        assertDoesNotThrow(() -> taskService.getTaskById(newTask.getTaskId()));
        assertEquals(tasksCount+1, taskRepository.count());
        assertEquals(newTask, taskService.getTaskById(taskId));
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
        when(taskRepository.existsById(taskId)).thenReturn(true);
        Task taskWithExistingTaskId = new Task(taskId,userId,"Title","Description", ZonedDateTime.now().plusHours(3));
        assertThrows(
                TaskAlreadyExistsException.class,
                () -> taskService.createTask(taskWithExistingTaskId)
        );
    }
    @Test
    void getTask_WithExistingTaskId_ShouldReturnTask() {

        long taskId = 1;
        Task expectedTask = new Task(taskId,1,"Title","Description", ZonedDateTime.now().plusHours(3));
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(expectedTask));
        Task actualTask = taskService.getTaskById(taskId);
        assertEquals(expectedTask, actualTask);//ignore that we can throw an exception because this fails the test anyway
    }
    @Test
    void getTask_WithNonExistentId_ShouldThrowException() {

        long invaldTaskId = 1;
        //Task task = new Task(taskId,1,"Title","Description", ZonedDateTime.now().plusHours(3));
        when(taskRepository.findById(invaldTaskId)).thenReturn(Optional.empty());
        //taskService.createTask(task);
        assertThrows(
                TaskNotFoundException.class, //what exception will be thrown
                () ->taskService.getTaskById(invaldTaskId));
    }



    @Test
    void getAllTasks_WithNoTasks_ShouldReturnEmptyList(){
        when(taskRepository.findAll()).thenReturn(List.of());
        assertTrue(taskService.getAllTasks().isEmpty());
    }

    @Test
    void getAllTasks_WithMultipleTasks_ShouldReturnListOfAllTasks(){
        long userId = 1;
        Task task1 = new Task(1,userId,"Title","Description", ZonedDateTime.now().plusHours(3));
        Task task2 = new Task(2,userId+1,"Title","Description", ZonedDateTime.now().plusDays(3).plusYears(1));
        when(taskRepository.findAll()).thenReturn(List.of(task1,task2));
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
        Task task1User1 = new Task(1,userId,"Title","Description", ZonedDateTime.now().plusHours(3));
        Task task2User1 = new Task(2,userId,"Title1","Description1", ZonedDateTime.now().plusHours(4));
        when(taskRepository.findByUserId(userId)).thenReturn(List.of(task1User1,task2User1));

        List<Task> userTasks = taskService.getTasksByUserId(userId);
        assertAll(
                ()->assertEquals(2,userTasks.size()),
                ()->assertTrue(userTasks.contains(task1User1)),
                ()->assertTrue(userTasks.contains(task2User1))
        );
    }

    @Test
    void getTasksByUserId_WithNonExistentUserId_ShouldReturnEmptyList() {
        long invalidUserId = -1;
        when(taskRepository.findByUserId(invalidUserId)).thenReturn(List.of());
        List<Task> userTasks = taskService.getTasksByUserId(invalidUserId);
        assertEquals(0,userTasks.size());

    }
    @Test
    void deleteTaskById_WithExistingTaskId_ShouldRemoveTask() {
        long userId = 1;
        long taskId = 1;
        Task taskId1User1 = new Task(taskId,userId ,"Title","Description", ZonedDateTime.now().plusHours(3));
        Task taskId2User2 = new Task(taskId+1,userId ,"Title1","Description1", ZonedDateTime.now().plusHours(4));
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(taskId1User1));
        Task deletedTask = taskService.deleteTaskById(taskId);
        when(taskRepository.findAll()).thenReturn(List.of(taskId2User2));
        List<Task> tasksWithoutFirstTask = taskService.getAllTasks();
        assertAll(
                ()->assertEquals(1,tasksWithoutFirstTask.size()),
                ()->assertFalse(tasksWithoutFirstTask.contains(taskId1User1)),
                ()->assertTrue(tasksWithoutFirstTask.contains(taskId2User2)),
                ()->assertEquals(deletedTask,taskId1User1)
                );
    }

    @Test
    void deleteTaskById_WithNonExistentTaskId_ShouldThrowException() {
        long invalidTaskId = -1;
        when(taskRepository.findById(invalidTaskId)).thenReturn(Optional.empty());
        assertThrows(TaskNotFoundException.class,
                ()->taskService.deleteTaskById(invalidTaskId));


    }
}
