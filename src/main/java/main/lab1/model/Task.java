package main.lab1.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.io.Serializable;
import java.time.ZonedDateTime;


@Getter
@Setter
@Entity //mandatory for SpringJPA
@Table(name = "tasks")
public class Task implements Serializable  { //serializable simple but not the best solution?

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long taskId;
    @Positive @Column(nullable = false)
    private long userId;
    @NotEmpty @Column(nullable = false, length = 100)
    private String taskTitle;
    @Column(length = 1000)
    private String taskDescription;
    @Setter(AccessLevel.NONE) @Column(nullable = false)
    private ZonedDateTime createdAt; // do I want to remove default now? since postgres can do it
    @Column(nullable = false)
    private ZonedDateTime expiresAt;
    @Column(nullable = false)
    private boolean isCompleted;

    public Task(){
        this.createdAt = ZonedDateTime.now();
        this.isCompleted = false;
    }

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
        this.createdAt = ZonedDateTime.now();
        this.isCompleted = false;
    }
}
