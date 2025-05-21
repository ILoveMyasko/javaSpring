package main.lab1.services;

import org.springframework.stereotype.Service;

@Service
public interface SchedulerService {
    void deleteOverdueCompletedTasks();
}
