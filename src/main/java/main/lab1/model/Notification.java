package main.lab1.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor //mandatory for SpringJPA
@AllArgsConstructor
@Entity
@Table(name = "notifications")
public class Notification {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    long notificationId;
    @Positive @NotNull
    long userId;
    @Positive @NotNull
    long taskId;
    @NotEmpty String text;
    final LocalDateTime createdAt = LocalDateTime.now();
    public Notification(long userId, long taskId, String message) {
        this.userId = userId;
        this.taskId = taskId;
        this.text = message;
    }
}
