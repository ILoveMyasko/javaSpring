package main.lab1.controllerTests;

import main.lab1.controllers.UserController;
import main.lab1.entities.User;
import main.lab1.exceptions.UserAlreadyExistsException;
import main.lab1.exceptions.UserNotFoundException;
import main.lab1.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.assertj.core.api.Assertions.assertThat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

//have to mock for unit tests?
@ExtendWith(MockitoExtension.class) //activate support of mocks (Junit5)
public class UserControllerTest {

    @Mock //mock userService(dummy)
    private UserService userService;

    @InjectMocks  //It just does this?
    //UserService mockService = Mockito.mock(UserService.class);
    //UserController controller = new UserController(mockService);
    private UserController userController;

    @Test
    void getAllUsers_WithNoUsers_ReturnsEmptyList() {
        when(userService.getAllUsers()).thenReturn(Collections.emptyList());
        ResponseEntity<List<User>> response = userController.getAllUsers(); // this will return what we mocked (previous command)

        assertThat(response.getBody())
                .isNotNull()
                .isEmpty();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userService, times(1)).getAllUsers();

    }

    @Test
    void getAllUsers_WithMultipleUsers_ReturnsListOfAllUsers() {
        List<User> mockUsers = Arrays.asList(
                new User(1, "John", "brutalkin_V@mail.ru"),
                new User(2, "Alice", "komondor25@gmail.com")
        );
        when(userService.getAllUsers()).thenReturn(mockUsers);
        ResponseEntity<List<User>> response = userController.getAllUsers(); // this will return what we mocked (previous command)

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThat(response.getBody())
                .isNotNull()
                .hasSize(2);
        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void getUserById_WithExistingId_ReturnsUser() {
        // Arrange
        int userId = 1;
        User mockUser = new User(userId, "John", "brutalkin_V@mail.ru");
        when(userService.getUserById(userId)).thenReturn(mockUser);

        ResponseEntity<User> response = userController.getUserById(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThat(response.getBody())
                .isNotNull()
                .isEqualTo(mockUser);

        verify(userService, times(1)).getUserById(userId);
    }

    @Test
    void getUserById_WithNonExistentId_ThrowsUserNotFoundException() {
        int invalidId = 999;
        when(userService.getUserById(invalidId))
                .thenThrow(new UserNotFoundException(invalidId));

        assertThrows(UserNotFoundException.class,
                () -> userController.getUserById(invalidId));
        // we cant check the http code because we don't actually create Spring MVC?
        /*
        UserNotFoundException ex = new UserNotFoundException("User not found");
        ResponseEntity<String> response = userController.handleUserNotFoundException(ex); //here not exception but userid
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("User not found", response.getBody());
         */
    }
    //to test @Valid need to create integration tests mockMvc?
    @Test
    void createUser_NewUser_CallsService() {

        User newUser = new User(1, "John", "brutalkin_V@mail.ru");
        doNothing().when(userService).createUser(newUser);

        assertDoesNotThrow(() -> userController.createUser(newUser));
        verify(userService, times(1)).createUser(newUser);
    }

    @Test
    void createUser_DuplicateUser_ThrowsUserAlreadyExistsException() {
        User duplicateUser = new User(1, "John", "brutalkin_V@mail.ru");

        doThrow(new UserAlreadyExistsException(1))
                .when(userService).createUser(duplicateUser);

        assertThrows(UserAlreadyExistsException.class,
                () -> userController.createUser(duplicateUser));
        verify(userService, times(1)).createUser(duplicateUser);
    }

    @Test
    void handleUserAlreadyExistsException_ReturnsConflictStatus() {
        int duplicateUserId = 1;
        UserAlreadyExistsException ex = new UserAlreadyExistsException(duplicateUserId);
        ResponseEntity<String> response = userController.handleUserAlreadyExistsException(ex);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    void handleUserNotFoundException_ReturnsNotFoundStatus() {
        int nonExistentUserId = 1;
        UserNotFoundException ex = new UserNotFoundException( nonExistentUserId);
        ResponseEntity<String> response = userController.handleUserNotFoundException(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
