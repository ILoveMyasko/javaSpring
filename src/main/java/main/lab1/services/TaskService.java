package main.lab1.services;

import main.lab1.model.Task;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;

@Service
public interface TaskService {

    Task getTaskById(long id);

    Task createTask(Task task);

    List<Task> getAllTasks();

    Task deleteTaskById(long id);

    List<Task> getTasksByUserId(long id);

    Task markAsCompleted(long id);

    List<Task> findExpiredCompletedTasks(ZonedDateTime now);
}
