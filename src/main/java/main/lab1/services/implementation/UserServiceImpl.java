package main.lab1.services.implementation;

import java.util.List;
import main.lab1.model.User;
import main.lab1.exceptions.DuplicateResourceException;
import main.lab1.exceptions.UserNotFoundException;
import main.lab1.repos.UserRepository;
import main.lab1.services.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Transactional
    public User createUser(User newUser) {
        if (userRepository.existsByEmail(newUser.getEmail()))
        {
            throw new DuplicateResourceException("User with email " + newUser.getEmail() + " already exists");
        }
        return userRepository.save(newUser);
    }

    @Transactional(readOnly = true)
    public User getUserById(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public boolean existsByUserId ( long id)
    {
        return userRepository.existsById(id);
    }

    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

}
