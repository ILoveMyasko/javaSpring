CREATE TABLE tasks (
                       task_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                       user_id BIGINT NOT NULL ,
                       task_title VARCHAR(100) NOT NULL,
                       task_description VARCHAR(255) NOT NULL,
                       created_at TIMESTAMP WITH TIME ZONE NOT NULL,
                       expires_at TIMESTAMP WITH TIME ZONE,
                       is_completed BOOLEAN NOT NULL

);