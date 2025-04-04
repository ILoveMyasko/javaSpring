package main.lab1.repos;

import jakarta.transaction.Transactional;
import main.lab1.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;

public interface NotificationRepository  extends JpaRepository<Notification,Long> {
    List<Notification> findByUserId(Long userId);
    List<Notification> findByTaskId(Long taskId);

   // @Transactional
    void deleteByTaskId(Long taskId);
}
