package main.lab1.services.implementation;

import lombok.AllArgsConstructor;
import main.lab1.model.Task;
import main.lab1.services.SchedulerService;
import main.lab1.services.TaskService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class SchedulerServiceImpl implements SchedulerService {

    TaskService taskService;

    @Async
    @Scheduled(initialDelay = 30000, fixedDelay = 60000)
    public void deleteOverdueCompletedTasks() {
        System.out.println("Deleting tasks...");
        List<Task> tasksToDelete = taskService.findExpiredCompletedTasks(ZonedDateTime.now());
        tasksToDelete.forEach(task ->
        {
            System.out.println("Deleting task with: taskId = " + task.getTaskId() + " userId = " + task.getUserId() );
            taskService.deleteTaskById(task.getTaskId());
        });
    }

}
