package main.lab1.repos;

import main.lab1.model.Task;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository {

    boolean existsById (long id);
    Task save (Task newTask);

    Optional<Task> findById(long id);
    List<Task> findAll();
    List<Task> findByUserId(long id);
    List<Task> findByUserIdAndIsCompletedFalse(long id);
    void deleteById(long id);
    List<Task> findByIsCompletedTrueAndExpiresAtBefore(ZonedDateTime moment);
}
