CREATE INDEX idx_task_user_id ON tasks(user_id);
CREATE INDEX idx_notification_task_id ON notifications(task_id);
CREATE INDEX idx_notification_user_id ON notifications(user_id);