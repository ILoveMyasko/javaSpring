package main.lab1.services;

import main.lab1.entities.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
//import java.util.Optional;

@Service
public interface UserService {

    User getUserById(int id);

    void createUser(User user);

    ArrayList<User> getAllUsers();


}
