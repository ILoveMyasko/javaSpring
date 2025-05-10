package main.lab1.services;

import main.lab1.entities.Notification;
import main.lab1.entities.Task;
import main.lab1.exceptions.DuplicateResourceException;
import main.lab1.exceptions.TaskNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TaskServiceImpl implements TaskService {

    NotificationService notificationService;
    private final Map<Integer, Task> tasks = new HashMap<>();

    public TaskServiceImpl(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    public Task getTaskById(int id) {
        if (!tasks.containsKey(id)) {
            throw new TaskNotFoundException(id);
        } else return tasks.get(id);
    }

    public Task createTask(Task newTask) {
        if (tasks.containsKey(newTask.getId())) {
            throw new DuplicateResourceException("Task with id " + newTask.getId() + " already exists");
        }
        tasks.put(newTask.getId(), newTask);
        notificationService.createNotification(new Notification(newTask.getUserId(), newTask.getId(), "Task created!"));
        return newTask;
    }

    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    public void deleteTaskById(int id) {
        Task task = tasks.remove(id);
        if (task!=null) {
            notificationService.createNotification(new Notification(task.getUserId(), task.getId(), "Task deleted!"));
        }
    }

    public List<Task> getTasksByUserId(int id) {
        return tasks.values().stream().filter(task -> task.getUserId() == id).toList();
    }
}
