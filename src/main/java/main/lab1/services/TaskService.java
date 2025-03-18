package main.lab1.services;

import main.lab1.entities.Task;
import main.lab1.entities.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public interface TaskService {

    Task getTaskById(int id);

    void createTask(Task task);

    List<Task> getAllTasks();

    void deleteTaskById(int id);

    List<Task> getTasksByUserId(int id);
}
