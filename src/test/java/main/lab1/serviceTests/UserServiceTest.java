package main.lab1.serviceTests;

import main.lab1.model.User;
import main.lab1.exceptions.UserAlreadyExistsException;
import main.lab1.exceptions.UserNotFoundException;
import main.lab1.services.UserService;
import main.lab1.services.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

//unit tests
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl();
    }

    @Test
    void createUser_WithNewId_ShouldAddUser() {
        long userId = 1;
        User user = new User(userId, "Alex", "alex@ex.com");
        long usersCount = userService.getAllUsers().size();
        userService.createUser(user);
        assertDoesNotThrow(() -> userService.getUserById(userId));
        assertEquals(user, userService.getUserById(userId));
        assertEquals(usersCount+1, userService.getAllUsers().size());
    }

    @Test
    void createUser_WithExistingId_ShouldThrowException() {
        long userId = 1;
        User user = new User(userId, "Alex", "alex@ex.com");
        User user2 = new User(userId, "Anna", "anna@example.com");
        userService.createUser(user);
        assertThrows(UserAlreadyExistsException.class,
                () -> userService.createUser(user2)
        );
    }

    @Test
    void getUser_WithExistingId_ShouldReturnUser() {
        User expectedUser = new User(1, "Alex", "alex@ex.com");
        userService.createUser(expectedUser);
        User actualUser = userService.getUserById(1);
        assertEquals(expectedUser, actualUser);//ignore that we can throw an exception because this fails the test anyway
    }

    @Test
    void getUser_WithNonExistentId_ShouldThrowException() {
        User user = new User(1, "Alex", "alex@ex.com");
        userService.createUser(user);
        assertThrows(UserNotFoundException.class, //what exception will be thrown
                () -> userService.getUserById(2));
    }

    @Test
    void getAllUsers_WithNoUsers_ShouldReturnEmptyList() {
        assertTrue(userService.getAllUsers().isEmpty());
    }

    @Test
    void getAllUsers_WithUsers_ShouldReturnListOfAllUsers() {
        User user1 = new User(1, "Alex", "alex@example.com");
        User user2 = new User(2, "Anna", "anna@example.com");
        userService.createUser(user1);
        userService.createUser(user2);
        List<User> users = userService.getAllUsers();
        assertAll(//assert there are exactly 2 users and both inserted are present
                () -> assertEquals(2, users.size()),
                () -> assertTrue(users.contains(user1)),
                () -> assertTrue(users.contains(user2)));

    }

}
