package main.lab1.exceptions;

public class TaskAlreadyExistsException extends RuntimeException {
  public TaskAlreadyExistsException(int id) {
    super("Task with id = " + id + " already exists.");
  }
}