package main.lab1.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class Notification {
    @Positive long notificationId;
    @Positive long userId;
    @Positive long taskId;
    @NotEmpty String text;
    final LocalDateTime createdAt = LocalDateTime.now();
}
