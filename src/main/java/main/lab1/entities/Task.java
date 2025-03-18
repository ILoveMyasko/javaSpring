package main.lab1.entities;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
public class Task {

    @NotNull int id;
    @NotNull int userId;
    @NotEmpty String taskTitle;
    @NotEmpty String taskDescription;
    final ZonedDateTime createdAt = ZonedDateTime.now();
    @Valid ZonedDateTime expiresAt;
    boolean isCompleted=false;
}
