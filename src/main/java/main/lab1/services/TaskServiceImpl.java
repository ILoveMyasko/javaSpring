package main.lab1.services;

import main.lab1.model.Task;
import main.lab1.model.Notification;
import main.lab1.exceptions.TaskAlreadyExistsException;
import main.lab1.exceptions.TaskNotFoundException;
import main.lab1.model.User;
import main.lab1.repos.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class TaskServiceImpl implements TaskService {

    //private final Map<Integer, Task> tasks = new HashMap<>();
    final private TaskRepository taskRepository;
    private final UserService userService;
    private final NotificationService notificationService;
    public TaskServiceImpl(TaskRepository taskRepository, UserService userService, NotificationService notificationService) {
        this.taskRepository = taskRepository;
        this.userService = userService;
        this.notificationService = notificationService;
    }

    public Task getTaskById(long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
    }

    // since im no longer in control of generating IDs for users, how do I know an id of the user to create a task for him?
    public Task createTask(Task task) {
        userService.getUserById(task.getUserId()); // Дописать
        if (taskRepository.existsById(task.getTaskId())) {
            throw new TaskAlreadyExistsException(task.getTaskId());
        }
        notificationService.createNotification(new Notification(task.getUserId(),task.getTaskId(), "Task created!"));
        return taskRepository.save(task);

    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public void deleteTaskById(long id) {
        Task task = taskRepository.deleteById(id);
        if (task!=null) {
            notificationService.createNotification(new Notification(task.getUserId(), task.getTaskId(), "Task deleted!"));
        }
    }

    public List<Task> getTasksByUserId(long id) {
        return taskRepository.findByUserId(id);
    }
}
