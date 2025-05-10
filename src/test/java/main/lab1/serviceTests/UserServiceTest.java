package main.lab1.serviceTests;


import main.lab1.model.User;
import main.lab1.exceptions.DuplicateResourceException;
import main.lab1.exceptions.UserNotFoundException;
import main.lab1.repos.UserRepository;
import main.lab1.services.implementation.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
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

    private User userToSave;
    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        userToSave = new User(1, "Alex", "alex@ex.com");
        user1 = new User(2, "Anna", "anna@example.com");
        user2 =  new User(3, "Jim", "jim@example.com");
    }

    @Test
    void createUser_WithNewId_ShouldAddUser() {
        when(userRepository.existsByEmail(userToSave.getEmail())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(userToSave);

        User savedUser = userService.createUser(userToSave);

        assertNotNull(savedUser);
        assertEquals(userToSave,savedUser);
        verify(userRepository).existsByEmail(savedUser.getEmail());
        verify(userRepository).save(savedUser);
    }

    @Test
    void createUser_WithExistingEmail_ShouldThrow()
    {
        when(userRepository.existsByEmail(userToSave.getEmail())).thenReturn(true);

        assertThrows(DuplicateResourceException.class,
                ()->userService.createUser(userToSave));

        verify(userRepository,never()).save(any(User.class));
    }

    @Test
    void getUser_WithExistingId_ShouldReturnUser() {

        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));

        User actualUser = userService.getUserById(1L);

        assertNotNull(actualUser);
        assertEquals(user1, actualUser);
        verify(userRepository).findById(1L);
    }

    @Test
    void getUser_WithNonExistentId_ShouldThrowException() {
        long newUserId = 3;
        when(userRepository.findById(newUserId)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class,
                () -> userService.getUserById(newUserId));
    }

    @Test
    void getAllUsers_WithNoUsers_ShouldReturnEmptyList() {
        when(userRepository.findAll()).thenReturn(List.of());

        List<User> allUsers = userService.getAllUsers();

        assertTrue(allUsers.isEmpty());
    }

    @Test
    void getAllUsers_WithUsers_ShouldReturnListOfAllUsers() {

        when(userRepository.findAll()).thenReturn(List.of(user1,user2));

        List<User> users = userService.getAllUsers();

        assertAll(
                () -> assertEquals(2, users.size()),
                () -> assertTrue(users.contains(user1)),
                () -> assertTrue(users.contains(user2)));

    }

}
