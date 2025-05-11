package main.lab1.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.*;
import java.time.ZonedDateTime;

@Getter
@Setter
@NoArgsConstructor //mandatory for SpringJPA
@AllArgsConstructor
@Entity
@Table(name = "notifications")
public class Notification {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long notificationId;
    @Positive @Column(nullable = false)
    long userId;
    @Positive @Column(nullable = false)
    long taskId;
    @NotEmpty @Column(length = 512)
    String text;
    @Setter(AccessLevel.NONE) @Column(nullable = false)
    ZonedDateTime createdAt = ZonedDateTime.now();

    @PrePersist
    private void onCreate(){
        this.createdAt = ZonedDateTime.now();
    }

    public Notification(long userId, long taskId, String message) {
        this.userId = userId;
        this.taskId = taskId;
        this.text = message;
        this.createdAt = ZonedDateTime.now();
    }
}
