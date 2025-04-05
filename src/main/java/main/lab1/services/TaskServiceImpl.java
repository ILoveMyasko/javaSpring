package main.lab1.services;

import main.lab1.model.Task;
import main.lab1.exceptions.TaskAlreadyExistsException;
import main.lab1.exceptions.TaskNotFoundException;
import main.lab1.repos.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class TaskServiceImpl implements TaskService {

    //private final Map<Integer, Task> tasks = new HashMap<>();
    final private TaskRepository taskRepository;
    private final UserService userService;

    public TaskServiceImpl(TaskRepository taskRepository, UserService userService) {
        this.taskRepository = taskRepository;
        this.userService = userService;
    }


    // since im no longer in control of generating IDs for users, how do I know an id of the user to create a task for him?
    @Caching(
            put = @CachePut(value = "tasks", key = "#result.taskId"),
            evict = @CacheEvict(value = "userTasks", key = "#task.userId") //force cache clear to avoid outdated data retrieval
    ) //no evict can cause us to get just this one added task if all other were deleted by timeout (outdating).
    public Task createTask(Task task) {
        userService.getUserById(task.getUserId()); // kinda like a check for existence, throws if not found
        if (taskRepository.existsById(task.getTaskId())) {
            throw new TaskAlreadyExistsException(task.getTaskId());
        }
        return taskRepository.save(task);
    }

    @Cacheable(value = "tasks", key = "#id") //check cache first, if not found put
    public Task getTaskById(long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
    }

    @Cacheable(value = "userTasks", key = "#id")
    public List<Task> getTasksByUserId(long id) {
        return taskRepository.findByUserId(id);
    }


    //ofc no caching
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    @Caching(
            evict = {
                    @CacheEvict(value = "tasks", key = "#id"), //update both caches
                    @CacheEvict(value = "userTasks", key = "#result.userId")
            })
    public Task deleteTaskById(long id) { //should it return or not? I don't think so
        //Task task = getTaskById(id);
        //taskRepository.deleteById(id);

        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
        taskRepository.deleteById(id);
        return task;
    }


}
