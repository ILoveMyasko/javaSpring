package main.lab1.services;

import main.lab1.model.Task;
import main.lab1.model.Notification;
import main.lab1.exceptions.DuplicateResourceException;
import main.lab1.exceptions.TaskNotFoundException;
import main.lab1.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TaskServiceImpl implements TaskService {

    private final Map<Long, Task> tasks = new HashMap<>();
    private final UserService userService;
    private final NotificationService notificationService;

    public TaskServiceImpl(NotificationService notificationService, UserService userService) {
        this.userService = userService;
        this.notificationService = notificationService;
    }

    public Task getTaskById(long id) {
        if (!tasks.containsKey(id)) {
            throw new TaskNotFoundException(id);
        } else return tasks.get(id);
    }

    public Task createTask(Task newTask) {
        userService.getUserById(newTask.getUserId()); // Дописать
        if (tasks.containsKey(newTask.getTaskId())) {
            throw new DuplicateResourceException("Task with id " + newTask.getTaskId() + " already exists");
        }
        tasks.put(newTask.getTaskId(), newTask);
        notificationService.createNotification(new Notification(newTask.getUserId(), newTask.getTaskId(), "Task created!"));
        return newTask;
    }

    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    public void deleteTaskById(long id) {
        Task task = tasks.remove(id);
        if (task!=null) {
            notificationService.createNotification(new Notification(task.getUserId(), task.getTaskId(), "Task deleted!"));
        }
    }

    public List<Task> getTasksByUserId(long id) {
        return tasks.values().stream().filter(task -> task.getUserId() == id).toList();
    }
}
