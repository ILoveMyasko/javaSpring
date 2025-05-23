package main.lab1.controllerTests;

import main.lab1.controllers.UserController;
import main.lab1.exceptions.ResourceNotFoundException;
import main.lab1.model.User;
import main.lab1.exceptions.DuplicateResourceException;
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
        ResponseEntity<List<User>> response = userController.getAllUsers();

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
        ResponseEntity<List<User>> response = userController.getAllUsers();

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
                .thenThrow(new ResourceNotFoundException("User with id" + invalidId + " not found"));

        assertThrows(ResourceNotFoundException.class,
                () -> userController.getUserById(invalidId));

    }
    //to test @Valid need to create integration tests mockMvc?
    @Test
    void createUser_NewUser_CallsService() {

        User newUser = new User(1, "John", "brutalkin_V@mail.ru");
        when(userService.createUser(newUser)).thenReturn(newUser);

        assertDoesNotThrow(() -> userController.createUser(newUser));
        verify(userService, times(1)).createUser(newUser);
    }

    @Test
    void createUser_DuplicateUser_ThrowsDuplicateResourceException() {
        long duplicateUserId = 1;
        User duplicateUser = new User(duplicateUserId, "John", "brutalkin_V@mail.ru");

        doThrow(new DuplicateResourceException("User with id " + duplicateUserId +" already exists"))
                .when(userService).createUser(duplicateUser);

        assertThrows(DuplicateResourceException.class,
                () -> userController.createUser(duplicateUser));
        verify(userService, times(1)).createUser(duplicateUser);
    }

    @Test
    void handleDuplicateResourceException_ReturnsConflictStatus() {
        int duplicateUserId = 1;
        DuplicateResourceException ex =
                new DuplicateResourceException("User with id " + duplicateUserId +" already exists");
        ResponseEntity<String> response = userController.handleDuplicateResourceException(ex);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    void handleUserNotFoundException_ReturnsNotFoundStatus() {
        int nonExistentUserId = 1;
        ResourceNotFoundException ex =
                new ResourceNotFoundException("User with id" + nonExistentUserId + " not found");
        ResponseEntity<String> response = userController.handleResourceNotFoundException(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
