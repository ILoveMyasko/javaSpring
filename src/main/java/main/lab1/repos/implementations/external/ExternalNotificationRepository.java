package main.lab1.repos.implementations.external;

import main.lab1.model.Notification;
import main.lab1.repos.NotificationRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;


@Profile({"h2","postgres"})
public interface ExternalNotificationRepository extends NotificationRepository, JpaRepository<Notification,Long> {

}
