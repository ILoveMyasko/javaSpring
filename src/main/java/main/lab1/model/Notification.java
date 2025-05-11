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
    Long notificationId;
    @Positive @Column(nullable = false)
    long userId;
    @Positive @Column(nullable = false)
    long taskId;
    @NotEmpty @Column(length = 1000)
    String text;
    @Setter(AccessLevel.NONE) @Column(nullable = false)
    ZonedDateTime createdAt = ZonedDateTime.now();


    private Notification(){
        this.createdAt = ZonedDateTime.now();
    }

    public Notification(long userId, long taskId, String message) {
        this.userId = userId;
        this.taskId = taskId;
        this.text = message;
        this.createdAt = ZonedDateTime.now();
    }
}
