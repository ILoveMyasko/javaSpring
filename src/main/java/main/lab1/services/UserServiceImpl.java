package main.lab1.services;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import main.lab1.entities.User;
import main.lab1.exceptions.UserAlreadyExistsException;
import main.lab1.exceptions.UserNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    //static?
    private final Map<Integer, User> users = new HashMap<Integer, User>(); // can skip <> params since java 7


    public User getUserById(int id) {
        if (!users.containsKey(id)) {
            throw new UserNotFoundException(id);
        } else return users.get(id);
    }

    public void createUser(User user) {
        if (users.containsKey(user.getId())) {
            throw new UserAlreadyExistsException(user.getId());
        }
        users.put(user.getId(), user);
    }

    public ArrayList<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    //useless?
//    public void deleteUserById(int id) {
//        users.remove(id);
//    }

}
