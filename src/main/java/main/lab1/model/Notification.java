package main.lab1.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor //mandatory for SpringJPA
@Entity
@Table(name = "notifications")
public class Notification {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    long notificationId;
    long userId;
    @Positive long taskId;
    @NotEmpty String text;
    final LocalDateTime createdAt = LocalDateTime.now();
    public Notification(long userId, long taskId, String message) {
        this.userId = userId;
        this.taskId = taskId;
        this.text = message;
    }
}
