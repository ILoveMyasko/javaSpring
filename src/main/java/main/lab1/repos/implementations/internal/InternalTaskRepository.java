package main.lab1.repos.implementations.internal;

import main.lab1.repos.TaskRepository;
import main.lab1.model.Task;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Repository
@Profile("internal")
public class InternalTaskRepository implements TaskRepository {
    private final Map<Long, Task> tasks = new HashMap<>();
    private static final AtomicLong idCounter = new AtomicLong();


    @Override
    public boolean existsById(long id) {
        return tasks.containsKey(id);
    }

    @Override
    public Task save(Task newTask) {
        newTask.setTaskId(idCounter.incrementAndGet());
        tasks.put(newTask.getTaskId(), newTask);
        return newTask;
    }

    @Override
    public Optional<Task> findById(long id) {
        return Optional.ofNullable(tasks.get(id));
    }

    @Override
    public List<Task> findAll() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Task> findByUserId(long id) {
        return tasks.values().stream().filter(task -> task.getUserId() == id).toList();
    }

    @Override
    public void deleteById(long id) {
       tasks.remove(id);
    }
}
