package main.lab1.exceptions;

public class TaskNotFoundException extends RuntimeException {
    public TaskNotFoundException(long id) {
        super("Task with id = " + id + " not found.");
    }
}