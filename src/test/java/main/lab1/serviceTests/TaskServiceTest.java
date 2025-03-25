package main.lab1.serviceTests;

import main.lab1.entities.Task;
import main.lab1.exceptions.TaskAlreadyExistsException;
import main.lab1.exceptions.TaskNotFoundException;
import main.lab1.services.TaskServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

//unit tests
public class TaskServiceTest {
    private TaskServiceImpl taskService;
    @BeforeEach
    void setUp() {
        taskService = new TaskServiceImpl(); // на лекции ходили, вовзращаемся в исходное состояние после каждого теста
    }

    @Test
    void createTask_WithNewId_ShouldAddTask() {
        //Task(int id, int userId, String taskTitle, String taskDescription, ZonedDateTime expiresAt)
        Task task = new Task(1,1,"Title","Description", ZonedDateTime.now().plusHours(1));

        taskService.createTask(task);
        // Assert
        assertDoesNotThrow(() -> taskService.getTaskById(1));
    }
    @Test
    void createTask_WithExistingId_ShouldThrowException() {
        Task task1 = new Task(1,1,"Title","Description", ZonedDateTime.now().plusHours(3));
        Task task2 = new Task(1,1,"Title","Description", ZonedDateTime.now().plusHours(3));
        taskService.createTask(task1);
        assertThrows(
                TaskAlreadyExistsException.class,
                () -> taskService.createTask(task2)
        );
    }
    @Test
    void getTask_WithExistingId_ShouldReturnTask() {
        Task expectedTask = new Task(1,1,"Title","Description", ZonedDateTime.now().plusHours(3));
        taskService.createTask(expectedTask);
        Task actualTask = taskService.getTaskById(1);
        assertEquals(expectedTask, actualTask);//ignore that we can throw an exception because this fails the test anyway
    }
    @Test
    void getTask_WithNonExistentId_ShouldThrowException() {
        Task task = new Task(1,1,"Title","Description", ZonedDateTime.now().plusHours(3));
        taskService.createTask(task);
        assertThrows(
                TaskNotFoundException.class, //what exception will be thrown
                () ->taskService.getTaskById(2));
    }



    @Test
    void getAllTasks_WithNoTasks_ShouldReturnEmptyList(){
        assertTrue(taskService.getAllTasks().isEmpty());
    }
    @Test
    void getAllTasks_WithMultipleTasks_ShouldReturnListOfAllTasks(){
        Task task1 = new Task(1,1,"Title","Description", ZonedDateTime.now().plusHours(3));
        Task task2 = new Task(2,1,"Title","Description", ZonedDateTime.now().plusDays(3).plusYears(1));
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
        Task task1User1 = new Task(1,1,"Title","Description", ZonedDateTime.now().plusHours(3));
        Task task2User1 = new Task(2,1,"Title1","Description1", ZonedDateTime.now().plusHours(4));
        Task task1User2 = new Task(3,2,"Title2","Description2", ZonedDateTime.now().plusHours(4));
        taskService.createTask(task1User1);
        taskService.createTask(task1User2);//another user have tasks
        taskService.createTask(task2User1);

        List<Task> userTasks = taskService.getTasksByUserId(1);
        assertAll(
                ()->assertEquals(2,userTasks.size()),
                ()->assertTrue(userTasks.contains(task1User1)),
                ()->assertTrue(userTasks.contains(task2User1))
        );
    }

    @Test
    void getTasksByUserId_WithNonExistentUserId_ShouldReturnEmptyList() {
        Task task1User1 = new Task(1,1,"Title","Description", ZonedDateTime.now().plusHours(3));
        Task task2User1 = new Task(2,1,"Title1","Description1", ZonedDateTime.now().plusHours(4));
        Task task1User2 = new Task(3,1,"Title1","Description1", ZonedDateTime.now().plusHours(4));
        taskService.createTask(task1User1);
        taskService.createTask(task1User2);//another user have tasks
        taskService.createTask(task2User1);
        List<Task> userTasks = taskService.getTasksByUserId(3);
        assertEquals(0,userTasks.size());

    }
    @Test
    void deleteTaskById_WithExistingTaskId_ShouldRemoveTask() {
        Task taskId1User1 = new Task(1,1,"Title","Description", ZonedDateTime.now().plusHours(3));
        Task taskId2User2 = new Task(2,1,"Title1","Description1", ZonedDateTime.now().plusHours(4));
        taskService.createTask(taskId1User1);
        taskService.createTask(taskId2User2);
        taskService.deleteTaskById(2);
        List<Task> userTasks = taskService.getAllTasks();
        assertAll(
                ()->assertEquals(1,userTasks.size()),
                ()->assertTrue(userTasks.contains(taskId1User1)),
                ()->assertFalse(userTasks.contains(taskId2User2))
                );
    }

    @Test
    void deleteTaskById_WithNonExistentTaskId_ShouldRemoveNothing() {
        Task taskId1User1 = new Task(1,1,"Title","Description", ZonedDateTime.now().plusHours(3));
        Task taskId2User2 = new Task(2,1,"Title1","Description1", ZonedDateTime.now().plusHours(4));
        taskService.createTask(taskId1User1);
        taskService.createTask(taskId2User2);
        taskService.deleteTaskById(3);
        List<Task> userTasks = taskService.getAllTasks();
        assertAll(
                ()->assertEquals(2,userTasks.size()),
                ()->assertTrue(userTasks.contains(taskId1User1)),
                ()->assertTrue(userTasks.contains(taskId2User2))
        );
    }
}
