package main.lab1.kafkaEvents;

public record TaskEvent(
        TaskEventTypeEnum eventType, // CREATE, DELETE, UPDATE
        Long taskId,
        Long userId
) {}

