package main.lab1.services.implementation;

import main.lab1.exceptions.ResourceNotFoundException;
import main.lab1.model.Task;
import main.lab1.model.Notification;
import main.lab1.repos.TaskRepository;

import main.lab1.services.NotificationService;
import main.lab1.services.TaskService;
import main.lab1.services.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserService userService;
    private final NotificationService notificationService;

    public TaskServiceImpl(TaskRepository taskRepository,
                           UserService userService,
                           NotificationService notificationService) {
        this.taskRepository = taskRepository;
        this.userService = userService;
        this.notificationService = notificationService;
    }

    @Transactional(readOnly = true)
    public Task getTaskById(long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task with id " + id + " not found"));
    }

    @Transactional
    public Task createTask(Task newTask) {
        if (!userService.existsByUserId(newTask.getUserId()))
        {
            throw new ResourceNotFoundException("User with id " + newTask.getUserId() + " not found");
        }
        Task savedTask =  taskRepository.save(newTask);
        notificationService.createNotification(
                new Notification(savedTask.getUserId(),savedTask.getTaskId(), "Task created!"));
        return savedTask;
    }

    @Transactional(readOnly = true)
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    @Transactional
    public void deleteTaskById(long id) {
        Optional<Task> taskOptional = taskRepository.findById(id);
        if (taskOptional.isPresent()) {
            Task taskToDelete = taskOptional.get();
            taskRepository.deleteById(id);
            notificationService.createNotification(
                    new Notification(taskToDelete.getUserId(), taskToDelete.getTaskId(), "Task deleted!"));
        }
        else throw new ResourceNotFoundException("Task with id " + id + " not found");
    }

    @Transactional
    public void markAsCompleted(long id)
    {
        Optional<Task> taskOptional = taskRepository.findById(id);
        if (taskOptional.isPresent()) {
            Task taskToMarkCompleted = taskOptional.get();
            taskToMarkCompleted.setCompleted(true);
            notificationService.createNotification(
                    new Notification(taskToMarkCompleted.getUserId(), taskToMarkCompleted.getTaskId(), "Task completed!"));
        }
        else throw new ResourceNotFoundException("Task with id " + id + " not found");
    }

    @Transactional(readOnly = true)
    public List<Task> getTasksByUserId(long id) {
        return taskRepository.findByUserIdAndIsCompletedFalse(id);
    }
}
