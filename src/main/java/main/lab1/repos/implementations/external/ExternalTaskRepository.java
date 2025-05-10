package main.lab1.repos.implementations.external;

import main.lab1.model.Task;
import main.lab1.repos.TaskRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

@Profile("h2")
public interface ExternalTaskRepository extends TaskRepository,JpaRepository<Task,Long> {

}
