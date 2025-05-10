package main.lab1.repos;

import jakarta.validation.constraints.Email;
import main.lab1.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
    boolean existsByEmail(String email);
}
