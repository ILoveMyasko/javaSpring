package main.lab1.services;

import main.lab1.model.Task;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TaskService {

    Task getTaskById(long id);

    void createTask(Task task);

    List<Task> getAllTasks();

    void deleteTaskById(long id);

    List<Task> getTasksByUserId(long id);
}
