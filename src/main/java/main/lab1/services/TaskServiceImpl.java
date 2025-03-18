package main.lab1.services;

import main.lab1.entities.Task;
import main.lab1.exceptions.TaskAlreadyExistsException;
import main.lab1.exceptions.TaskNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TaskServiceImpl implements TaskService {

    private final Map<Integer, Task> tasks = new HashMap<>();

    public Task getTaskById(int id) {
        if (!tasks.containsKey(id)) {
            throw new TaskNotFoundException(id);
        } else return tasks.get(id);
    }

    //how to check whether user exists?
    public void createTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            throw new TaskAlreadyExistsException(task.getId());
        }
        tasks.put(task.getId(), task);
    }

    public List<Task> getAllTasks() {

        return new ArrayList<>(tasks.values());
    }

    public void deleteTaskById(int id) {
        tasks.remove(id);
    }

    public List<Task> getTasksByUserId(int id) {
        return tasks.values().stream().filter(task -> task.getUserId() == id).toList();
    }
}
