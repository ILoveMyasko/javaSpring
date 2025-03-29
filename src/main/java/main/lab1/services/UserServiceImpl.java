package main.lab1.services;

import java.util.List;
import main.lab1.model.User;
import main.lab1.exceptions.UserAlreadyExistsException;
import main.lab1.exceptions.UserNotFoundException;
import main.lab1.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(User user) {
        // Проверяем существование пользователя по ID
        if (userRepository.existsById(user.getId())) {
            throw new UserAlreadyExistsException(user.getId());
        }
        return userRepository.save(user);
    }

    public User getUserById(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

}
