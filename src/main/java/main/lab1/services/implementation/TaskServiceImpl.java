package main.lab1.services.implementation;

import main.lab1.exceptions.ResourceNotFoundException;
import main.lab1.model.Task;
import main.lab1.model.Notification;
import main.lab1.repos.TaskRepository;

import main.lab1.services.NotificationService;
import main.lab1.services.TaskService;
import main.lab1.services.UserService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
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
    @Cacheable(value = "tasks", key = "#id")
    public Task getTaskById(long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task with id " + id + " not found"));
    }

    @Transactional
    @Caching(
            put = @CachePut(value = "tasks", key = "#result.taskId"),
            evict = @CacheEvict(value = "userTasks", key = "#newTask.userId") //force cache clear to avoid outdated data retrieval
    )
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
    @Caching(
            evict = {
                    @CacheEvict(value = "tasks", key = "#id"),
                    @CacheEvict(value = "userTasks", key = "#result.userId")
            })
    public Task deleteTaskById(long id) {
        Task taskToDelete = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task with id " + id + " not found"));
        taskRepository.deleteById(id);
        return taskToDelete;
    }

    @Transactional
    @Caching(
            put = {
                    @CachePut(value = "tasks", key = "#result.taskId")
            },
            evict = {
                    @CacheEvict(value = "userTasks", key = "#result.userId")
            })
    public Task markAsCompleted(long id)
    {
        Task taskToUpdate = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task with id " + id + " not found"));
        taskToUpdate.setCompleted(true);
        notificationService.createNotification(
                new Notification(taskToUpdate.getUserId(), taskToUpdate.getTaskId(), "Task completed!"));
        return taskToUpdate;
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "userTasks", key = "#id")
    public List<Task> getTasksByUserId(long id) {
        return taskRepository.findByUserIdAndIsCompletedFalse(id);
    }
}
