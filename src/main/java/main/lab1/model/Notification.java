package main.lab1.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data

@NoArgsConstructor //mandatory for SpringJPA
@Entity


@Table(name = "notifications")
public class Notification {
    private @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    long notificationId;
    private @Positive long userId;
    private @Positive long taskId;
    private @NotEmpty @Column(length = 255) String text; //255 is default
    private LocalDateTime createdAt = LocalDateTime.now();

    public Notification(long notificationId, long userId, long taskId, String text) {
        this.notificationId =notificationId;
        this.userId = userId;
        this.taskId = taskId;
        this.text = text;
        // createdAt = .now default
    }
}
