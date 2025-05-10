package main.lab1.repos;



import main.lab1.model.Task;

import java.util.List;
import java.util.Optional;

public interface TaskRepository {

    boolean existsById (long id);
    Task save (Task newTask);
    Optional<Task> findById(long id);
    List<Task> findAll();
    List<Task> findByUserId(long id);
    void deleteById(long id);
}
