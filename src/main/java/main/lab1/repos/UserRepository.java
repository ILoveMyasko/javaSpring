package main.lab1.repos;

import main.lab1.model.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository {

    boolean existsById (long id);
    boolean existsByEmailIgnoreCase (String email);
    User save (User newUser);
    Optional<User> findById(long id);
    List<User> findAll();


}
