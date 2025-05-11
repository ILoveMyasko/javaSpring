package main.lab1.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.time.ZonedDateTime;

@Getter
@Setter
@Entity
@Table(name = "notifications")
public class Notification {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationId;
    @Positive @Column(nullable = false)
    private long userId;
    @Positive @Column(nullable = false)
    private Long taskId;
    @NotEmpty @Column(length = 1000)
    private String text;
    @Setter(AccessLevel.NONE) @Column(nullable = false)
    private ZonedDateTime createdAt;


    public Notification(){
        this.createdAt = ZonedDateTime.now();
    }

    public Notification( long userId, long taskId, String message) {
        this.userId = userId;
        this.taskId = taskId;
        this.text = message;
        this.createdAt = ZonedDateTime.now();
    }

    public Notification(long notificationId,  long userId, long taskId, String message) {
        this.notificationId = notificationId;
        this.userId = userId;
        this.taskId = taskId;
        this.text = message;
        this.createdAt = ZonedDateTime.now();
    }
}
