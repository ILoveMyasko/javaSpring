package main.lab1.services;

import main.lab1.model.Task;
import main.lab1.model.Notification;
import main.lab1.exceptions.DuplicateResourceException;
import main.lab1.exceptions.TaskNotFoundException;
import main.lab1.model.User;
import main.lab1.repos.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;


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
    public Task createTask(Task newTask) {
        userService.getUserById(newTask.getUserId()); // Дописать
        if (taskRepository.existsById(newTask.getTaskId())) {
            throw new DuplicateResourceException("Task with id " + newTask.getTaskId() + " already exists");
        }
        Task savedTask =  taskRepository.save(newTask);
        notificationService.createNotification(new Notification(savedTask.getUserId(),savedTask.getTaskId(), "Task created!"));
        return savedTask;

    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public void deleteTaskById(long id) {
        Optional<Task> taskOptional = taskRepository.findById(id);
        if (taskOptional.isPresent()) {
            Task taskToDelete = taskOptional.get();
            long userId = taskToDelete.getUserId();
            long taskId = taskToDelete.getTaskId();

            taskRepository.deleteById(id);

            notificationService.createNotification(new Notification(userId, taskId, "Task deleted!"));
        }
        else throw new TaskNotFoundException(id);
    }

    public List<Task> getTasksByUserId(long id) {
        return taskRepository.findByUserId(id);
    }
}
