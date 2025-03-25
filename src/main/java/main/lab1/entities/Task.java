package main.lab1.entities;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.ZonedDateTime;

@Data
@EqualsAndHashCode
public class Task {

    @Positive int id; //primitives cannot be null
    @Positive int userId;
    @NotEmpty String taskTitle;
    @NotEmpty String taskDescription;
    final ZonedDateTime createdAt = ZonedDateTime.now();
    ZonedDateTime expiresAt;
    boolean isCompleted=false; //redundant?

    @AssertTrue(message = "expiresAt must be after createdAt")
    private boolean isExpiresAtValid() {
        return expiresAt == null || expiresAt.isAfter(createdAt);
    }

    public Task(int id, int userId, String taskTitle, String taskDescription, ZonedDateTime expiresAt) {
        this.id = id;
        this.userId = userId;
        this.taskTitle = taskTitle;
        this.taskDescription = taskDescription;
        this.expiresAt = expiresAt;
        // createdAt = .now
        // isCompleted false default
    }
}
