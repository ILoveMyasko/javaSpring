package main.lab1.services.implementation;

import main.lab1.exceptions.ResourceNotFoundException;
import main.lab1.kafkaEvents.TaskEvent;
import main.lab1.kafkaEvents.TaskEventTypeEnum;
import main.lab1.model.Task;
import main.lab1.repos.TaskRepository;

import main.lab1.services.TaskService;
import main.lab1.services.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;


@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserService userService;
    private final KafkaTemplate<String, TaskEvent> kafkaTemplate;
    private final String taskEventTopic;

    public TaskServiceImpl(TaskRepository taskRepository,
                           UserService userService,
                           KafkaTemplate<String, TaskEvent> kafkaTemplate,
                           @Value("${kafka.topic.task-event}") String taskEventTopic) {
        this.taskRepository = taskRepository;
        this.userService = userService;
        this.kafkaTemplate = kafkaTemplate;
        this.taskEventTopic = taskEventTopic;
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

        kafkaTemplate.send(taskEventTopic, new TaskEvent(
                TaskEventTypeEnum.CREATE,
                savedTask.getTaskId(),
                savedTask.getUserId()));

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
        kafkaTemplate.send(taskEventTopic, new TaskEvent(
                TaskEventTypeEnum.UPDATE,
                taskToUpdate.getTaskId(),
                taskToUpdate.getUserId()));
        return taskToUpdate;
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "userTasks", key = "#id")
    public List<Task> getTasksByUserId(long id) {
        return taskRepository.findByUserIdAndIsCompletedFalse(id);
    }
}
