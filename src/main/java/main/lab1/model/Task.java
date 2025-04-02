package main.lab1.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@EqualsAndHashCode
@Entity
@NoArgsConstructor //mandatory for SpringJPA
@Table(name = "tasks")
public class Task {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    long taskId;
    @Positive long userId; // do I want to check it through autowiring services? or do I migrate all checks to db and make foreign keys
    @NotEmpty @Column(length = 100) String taskTitle;
    @NotEmpty @Column(length = 255) String taskDescription;
    final ZonedDateTime createdAt = ZonedDateTime.now(); // do I want to remove default now? since postgres can autogen it
    ZonedDateTime expiresAt;
    boolean isCompleted = false;

    @AssertTrue(message = "expiresAt must be after createdAt")
    private boolean isExpiresAtValid() {
        return expiresAt == null || expiresAt.isAfter(createdAt);
    }

    public Task(long id, long userId, String taskTitle, String taskDescription, ZonedDateTime expiresAt) {
        this.taskId = id;
        this.userId = userId;
        this.taskTitle = taskTitle;
        this.taskDescription = taskDescription;
        this.expiresAt = expiresAt;
        // createdAt = .now default
        // isCompleted = false default
    }
}
