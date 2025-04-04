package main.lab1.kafkaEvents;
//requires java 17+
    public record TaskEvent(
            TaskEventTypeEnum eventType, // CREATE, DELETE, UPDATE
            Long taskId,
            Long userId
    ) {}
