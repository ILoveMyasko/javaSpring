package main.lab1.services;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import main.lab1.model.User;
import main.lab1.exceptions.UserAlreadyExistsException;
import main.lab1.exceptions.UserNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    //static?
    private final Map<Long, User> users = new HashMap<>(); // can skip <> params since java 7

    public void createUser(User user) {
        if (users.containsKey(user.getUserId())) {
            throw new UserAlreadyExistsException(user.getUserId());
        }
        users.put(user.getUserId(), user);
    }
    //streams don't create a copy
    public User getUserById(long id) {
        if (!users.containsKey(id)) {
            throw new UserNotFoundException(id);
        } else return users.get(id);
    }


    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }


}
