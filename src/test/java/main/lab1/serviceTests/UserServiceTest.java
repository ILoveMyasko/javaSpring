package main.lab1.serviceTests;

import main.lab1.model.User;
import main.lab1.exceptions.UserAlreadyExistsException;
import main.lab1.exceptions.UserNotFoundException;
import main.lab1.repos.UserRepository;
import main.lab1.services.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

//unit tests
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;


    @Test
    void createUser_WithNewId_ShouldAddUser() {
        long newUserId = 1;
        User user = new User(newUserId, "Alex", "alex@ex.com");


        when(userRepository.findById(newUserId)).thenReturn(Optional.empty());
        when(userRepository.existsById(newUserId)).thenReturn(false);
        when(userRepository.save(user)).thenReturn(user);
        when(userRepository.findAll())
                .thenReturn(List.of()); // before creation
        long initialUserCount = userRepository.count();

        userService.createUser(user);

        when(userRepository.findById(newUserId)).thenReturn(Optional.of(user));
        when(userRepository.findAll())
                .thenReturn(List.of(user));


        assertDoesNotThrow(() -> userService.getUserById(newUserId));
        assertEquals(user, userService.getUserById(newUserId));
        assertEquals(initialUserCount+1, userService.getAllUsers().size());
    }

    @Test
    void createUser_WithExistingId_ShouldThrowException() {
        long existingUserId = 1;
        User newUser = new User(existingUserId, "NewUser", "new@ex.com");

        when(userRepository.existsById(existingUserId)).thenReturn(true);
        assertThrows(UserAlreadyExistsException.class, () -> {
            userService.createUser(newUser);
        });

        verify(userRepository, never()).save(any());
        verify(userRepository).existsById(existingUserId); // was called
    }

    @Test
    void getUser_WithExistingId_ShouldReturnUser() {
        long existingUserId = 1;
        User expectedUser = new User(existingUserId, "Alex", "alex@ex.com");
        when(userRepository.findById(existingUserId)).thenReturn( Optional.of(expectedUser));
        userService.createUser(expectedUser);
        User actualUser = userService.getUserById(existingUserId);
        assertEquals(expectedUser, actualUser);
    }

    @Test
    void getUser_WithNonExistentId_ShouldThrowException() {
        long newUserId = 1;
        //User user = new User(newUserId, "Alex", "alex@ex.com");
        when(userRepository.findById(newUserId)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, //what exception will be thrown
                () -> userService.getUserById(newUserId));
    }

    @Test
    void getAllUsers_WithNoUsers_ShouldReturnEmptyList() {
        when(userRepository.findAll()).thenReturn(List.of());
        assertTrue(userService.getAllUsers().isEmpty());
    }

    @Test
    void getAllUsers_WithUsers_ShouldReturnListOfAllUsers() {
        long userId = 1;
        User user1 = new User(userId, "Alex", "alex@example.com");
        User user2 = new User(userId+1, "Anna", "anna@example.com");

        when(userRepository.existsById(userId)).thenReturn(false);
        when(userRepository.existsById(userId+1)).thenReturn(false);
        userService.createUser(user1);
        userService.createUser(user2);
        when(userRepository.findAll()).thenReturn(List.of(user1,user2));
        List<User> users = userService.getAllUsers();
        assertAll(//assert there are exactly 2 users and both inserted are present
                () -> assertEquals(2, users.size()),
                () -> assertTrue(users.contains(user1)),
                () -> assertTrue(users.contains(user2)));

    }

}
