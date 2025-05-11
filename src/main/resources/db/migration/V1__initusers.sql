CREATE TABLE users
(
    user_id BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL,
    name    VARCHAR(50) NOT NULL,
    email   VARCHAR(50) NOT NULL,
    CONSTRAINT pk_users PRIMARY KEY (user_id)
);

CREATE TABLE tasks (
                       task_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                       user_id BIGINT NOT NULL ,
                       task_title VARCHAR(100) NOT NULL,
                       task_description VARCHAR(255),
                       created_at TIMESTAMP WITH TIME ZONE NOT NULL,
                       expires_at TIMESTAMP WITH TIME ZONE NOT NULL,
                       is_completed BOOLEAN NOT NULL
);

CREATE TABLE notifications
(
    notification_id BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL,
    user_id         BIGINT NOT NULL,
    task_id         BIGINT NOT NULL,
    text            VARCHAR(255),
    created_at      TIMESTAMP WITH TIME ZONE,
    CONSTRAINT pk_notifications PRIMARY KEY (notification_id)
);

