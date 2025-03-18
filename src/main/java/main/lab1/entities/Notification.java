package main.lab1.entities;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class Notification {
    @NotEmpty String text;
    @NotNull int userId;
    @NotNull int taskId;
    final LocalDateTime createdAt = LocalDateTime.now();
}
