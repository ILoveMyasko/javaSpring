package main.lab1.repos;

import main.lab1.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.ZonedDateTime;
import java.util.List;
//@Repository?
public interface TaskRepository extends JpaRepository<Task,Long> {
    List<Task> findByUserId(Long userId);

    @Query("SELECT t FROM Task t WHERE t.expiresAt < :now AND t.isCompleted = false") //sql injection impenetrable
    List<Task> findByDueDateBeforeAndCompletedFalse(@Param("now") ZonedDateTime now);

}
