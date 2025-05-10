package main.lab1.services;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import main.lab1.model.User;
import main.lab1.exceptions.DuplicateResourceException;
import main.lab1.exceptions.UserNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final Map<Long, User> users = new HashMap<>(); // can skip <> params since java 7

    public User createUser(User newUser) {
        if (users.containsKey(newUser.getUserId())) {
            throw new DuplicateResourceException("User with id " + newUser.getUserId() + " already exists");
        }
        if (users.values()
                 .stream()
                 .anyMatch(user -> user.getEmail().equalsIgnoreCase(newUser.getEmail())))
        {
            throw new DuplicateResourceException("User with email " + newUser.getEmail() + " already exists");
        }

        users.put(newUser.getUserId(), newUser);
        return newUser;
    }

    public User getUserById(long id) {
        if (!users.containsKey(id)) {
            throw new UserNotFoundException(id);
        } else return users.get(id);
    }


    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }


}
