package main.lab1.repos.implementations;

import main.lab1.model.User;
import main.lab1.repos.UserRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

@Profile("h2")
public interface ExternalUserRepository extends UserRepository, JpaRepository<User,Long> {
}
