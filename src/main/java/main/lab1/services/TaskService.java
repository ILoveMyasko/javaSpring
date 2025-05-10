package main.lab1.services;

import main.lab1.entities.Task;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TaskService {

    Task getTaskById(int id);

    Task createTask(Task task);

    List<Task> getAllTasks();

    void deleteTaskById(int id);

    List<Task> getTasksByUserId(int id);
}
