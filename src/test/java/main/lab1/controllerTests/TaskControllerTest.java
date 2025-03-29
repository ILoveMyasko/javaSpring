package main.lab1.controllerTests;

import main.lab1.controllers.TaskController;
import main.lab1.model.Task;
import main.lab1.exceptions.TaskAlreadyExistsException;
import main.lab1.exceptions.TaskNotFoundException;
import main.lab1.services.TaskService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

//have to mock for unit tests?
@ExtendWith(MockitoExtension.class) //activate support of mocks (Junit5)
public class TaskControllerTest {

    @Mock //mock taskService(dummy)
    private TaskService taskService;

    @InjectMocks  //It just does this?
    //TaskService mockService = Mockito.mock(TaskService.class);
    //TaskController controller = new TaskController(mockService);
    private TaskController taskController;

    @Test
    void getAllTasks_WithNoTasks_ReturnsEmptyList() {
        when(taskService.getAllTasks()).thenReturn(Collections.emptyList());
        ResponseEntity<List<Task>> response = taskController.getAllTasks(); // this will return what we mocked (previous command)

        assertThat(response.getBody())
                .isNotNull()
                .isEmpty();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(taskService, times(1)).getAllTasks();

    }
    @Test
    void getAllTasks_WithMultipleTasks_ReturnsEntityWithListOfAllTasks() {
        List<Task> mockTasks = Arrays.asList(
                new Task(1,1,"Title","Description", ZonedDateTime.now().plusHours(3)),
                new Task(2,1,"Title","Description", ZonedDateTime.now().plusHours(3))
        );

        when(taskService.getAllTasks()).thenReturn(mockTasks);
        ResponseEntity<List<Task>> response = taskController.getAllTasks();

        assertThat(response.getBody())
                .isNotNull()
                .hasSize(2);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(taskService, times(1)).getAllTasks();
    }

    @Test
    void getTaskById_WithExistingId_ReturnsEntityWithTask() {
        // Arrange
        int taskId = 1;
        Task mockTask = new Task(1,1,"Title","Description", ZonedDateTime.now().plusHours(3));

        when(taskService.getTaskById(taskId)).thenReturn(mockTask);
        ResponseEntity<Task> response = taskController.getTaskById(taskId);

        assertThat(response.getBody())
                .isNotNull()
                .isEqualTo(mockTask);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(taskService, times(1)).getTaskById(taskId);
    }

    @Test
    void getTaskById_WithNonExistentId_ThrowsTaskNotFoundException() {
        int invalidId = 999;
        when(taskService.getTaskById(invalidId))
                .thenThrow(new TaskNotFoundException(invalidId));

        assertThrows(TaskNotFoundException.class,
                () -> taskController.getTaskById(invalidId));
    }

    @Test
    void getAllTasksByUserId_ForExistingUser_ReturnsEntityWithListOfAllUsersTasks() {
        int userId = 1;


        Task task1User1 = new Task(1,userId,"Title1","Description1", ZonedDateTime.now().plusHours(3));
        Task task2User1 = new Task(2,userId,"Title2","Description2", ZonedDateTime.now().plusHours(3));
        Task task1User2 = new Task(3,userId+1,"Title3","Description3", ZonedDateTime.now().plusHours(3));

        when(taskService.getTasksByUserId(userId)).thenReturn(
                Arrays.asList(task1User1,  task2User1)
        );

        ResponseEntity<List<Task>> response = taskController.getAllTasksByUserId(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThat(response.getBody())
                .isNotNull()
                .isNotEmpty()
                .hasSize(2)
                .containsExactlyInAnyOrder(task1User1, task2User1)
                .doesNotContain(task1User2);

        //?
        verify(taskService, times(1)).getTasksByUserId(userId);
    }
    @Test
    void getAllTasksByUserId_ForNonExistentUser_ReturnsEntityWithEmptyList() {
        int userId = 999;
        when(taskService.getTasksByUserId(userId)).thenReturn(Collections.emptyList());

        ResponseEntity<List<Task>> response = taskController.getAllTasksByUserId(userId);
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertThat(response.getBody())
                .isNotNull() //its not null just empty
                .isEmpty();
        verify(taskService,times(1)).getTasksByUserId(userId);

    }
    //to test @Valid need to create integration tests mockMvc?
    @Test
    void createTask_NewTask_CallsService() {

        Task newTask = new Task(1,1,"Title","Description", ZonedDateTime.now().plusHours(3));
        doNothing().when(taskService).createTask(newTask);

        assertDoesNotThrow(
                () -> taskController.createTask(newTask)
        );
        verify(taskService, times(1)).createTask(newTask);
    }

    @Test
    void createTask_DuplicateTask_ThrowsTaskAlreadyExistsException() {
        int duplicateTaskId = 1;
        Task duplicateTask = new Task(duplicateTaskId,1,"Title","Description", ZonedDateTime.now().plusHours(3));

        doThrow(new TaskAlreadyExistsException(duplicateTaskId))
                .when(taskService).createTask(duplicateTask);

        assertThrows(TaskAlreadyExistsException.class,
                () -> taskController.createTask(duplicateTask));
        verify(taskService, times(1)).createTask(duplicateTask);//is there really any reason to check that?
    }


    @Test
    void deleteTask_ForExistingTask_CallsService()
    {
        int taskId = 1;
        Task task = new Task(taskId,1,"Title","Description", ZonedDateTime.now().plusHours(3));

        doNothing().when(taskService).deleteTaskById(taskId);

        assertDoesNotThrow(
                ()->taskController.deleteTask(taskId)
        );
        verify(taskService, times(1)).deleteTaskById(taskId);
    }

    @Test
    void deleteTask_ForNonExistentTask_CallsService()
    {
        int nonExistentTaskId = 999;

        doThrow(new TaskNotFoundException(nonExistentTaskId))
                .when(taskService).deleteTaskById(nonExistentTaskId);

        assertThrows(TaskNotFoundException.class,
                ()->taskController.deleteTask(nonExistentTaskId)
        );
        verify(taskService, times(1)).deleteTaskById(nonExistentTaskId);//?
    }

    @Test
    void handleTaskAlreadyExistsException_ReturnsConflictStatus() {
        int duplicateTaskId = 1;
        TaskAlreadyExistsException ex = new TaskAlreadyExistsException( duplicateTaskId );
        ResponseEntity<String> response = taskController.handleTaskAlreadyExistsException(ex);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    void handleTaskNotFoundException_ReturnsNotFoundStatus() {
        int nonExistentTaskId = 1;
        TaskNotFoundException ex = new TaskNotFoundException( nonExistentTaskId);
        ResponseEntity<String> response = taskController.handleTaskNotFoundException(ex);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
