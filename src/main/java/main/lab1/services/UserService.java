package main.lab1.services;

import main.lab1.model.User;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public interface UserService {

    User getUserById(int id);

    void createUser(User user);

   List<User> getAllUsers();


}
