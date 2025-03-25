package main.lab1.entities;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class Notification {
    @Positive int userId;
    @Positive int taskId;
    @NotEmpty String text;
    final LocalDateTime createdAt = LocalDateTime.now();
}
