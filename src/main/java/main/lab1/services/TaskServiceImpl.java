package main.lab1.services;

import main.lab1.model.Task;
import main.lab1.exceptions.TaskAlreadyExistsException;
import main.lab1.exceptions.TaskNotFoundException;
import main.lab1.repos.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class TaskServiceImpl implements TaskService {

    final private TaskRepository taskRepository;
    @Autowired
    private UserService userService;



    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Task getTaskById(long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
    }

    // since im no longer in control of generating IDs for users, how do I know an id of the user to create a task for him?
    public Task createTask(Task task) {
        userService.getUserById(task.getUserId()); // Дописать
        if (taskRepository.existsById(task.getId())) {
            throw new TaskAlreadyExistsException(task.getId());
        }
        return taskRepository.save(task);
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public void deleteTaskById(long id) {
        taskRepository.deleteById(id);//ignored if not found. throw is definitely not a good idea
    }

    public List<Task> getTasksByUserId(long id) {
        return taskRepository.findByUserId(id);
    }
}
