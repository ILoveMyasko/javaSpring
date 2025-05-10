package main.lab1.repos;

import main.lab1.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    boolean existsById (long id);
    boolean existsByEmail (String email);
    User save (User newUser);
    Optional<User> findById(long id);
    List<User> findAll();


}
