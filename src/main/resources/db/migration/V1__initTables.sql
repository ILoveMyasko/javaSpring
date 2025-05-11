CREATE TABLE users
(
    user_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name    VARCHAR(50) NOT NULL,
    email   VARCHAR(50) NOT NULL,
    registered_at TIMESTAMP WITH TIME ZONE NOT NULL
);
CREATE TABLE tasks (
                       task_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                       user_id BIGINT NOT NULL ,
                       task_title VARCHAR(100) NOT NULL,
                       task_description VARCHAR(1000),
                       created_at TIMESTAMP WITH TIME ZONE NOT NULL,
                       expires_at TIMESTAMP WITH TIME ZONE NOT NULL,
                       is_completed BOOLEAN NOT NULL,
                       CONSTRAINT fk_tasks_user FOREIGN KEY(user_id) REFERENCES users(user_id)
);
CREATE TABLE notifications
(
    notification_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id         BIGINT NOT NULL ,
    task_id         BIGINT NOT NULL ,
    text            VARCHAR(1000),
    created_at      TIMESTAMP WITH TIME ZONE NOT NULL,
    CONSTRAINT fk_notifications_user FOREIGN KEY(user_id) REFERENCES users(user_id),
    CONSTRAINT fk_notifications_task FOREIGN KEY(task_id) REFERENCES tasks(task_id) ON DELETE CASCADE
);


