package main.lab1.repositoryTests.internal;

import main.lab1.model.Notification;
import main.lab1.repos.implementations.internal.InternalNotificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

public class InternalNotificationRepositoryTest {

    private InternalNotificationRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InternalNotificationRepository();
    }

    @Test
    void shouldAssignIdsStartingFromOneAndIncrement() {
        Notification n1 = new Notification();
        n1.setUserId(10L);
        n1.setTaskId(100L);
        Notification saved1 = repository.save(n1);

        Notification n2 = new Notification();
        n2.setUserId(20L);
        n2.setTaskId(200L);
        Notification saved2 = repository.save(n2);

        assertThat(saved1.getNotificationId()).isEqualTo(1L);
        assertThat(saved2.getNotificationId()).isEqualTo(2L);

        assertThat(repository.findAll())
                .extracting(Notification::getNotificationId)
                .containsExactly(1L, 2L);
    }

    @Test
    void shouldReturnAllNotifications() {
        Notification n1 = new Notification(); n1.setUserId(1L); n1.setTaskId(10L);
        Notification n2 = new Notification(); n2.setUserId(2L); n2.setTaskId(20L);
        repository.save(n1);
        repository.save(n2);

        List<Notification> all = repository.findAll();
        assertThat(all).hasSize(2)
                .extracting(Notification::getUserId, Notification::getTaskId)
                .containsExactlyInAnyOrder(
                        tuple(1L, 10L),
                        tuple(2L, 20L)
                );
    }

    @Test
    void shouldFindByUserId() {
        Notification a = new Notification(); a.setUserId(100L); a.setTaskId(1L);
        Notification b = new Notification(); b.setUserId(200L); b.setTaskId(2L);
        Notification c = new Notification(); c.setUserId(100L); c.setTaskId(3L);
        repository.save(a); // ID=1
        repository.save(b); // ID=2
        repository.save(c); // ID=3

        List<Notification> user100 = repository.findByUserId(100L);
        assertThat(user100).hasSize(2)
                .allMatch(n -> n.getUserId() == 100L)
                .extracting(Notification::getNotificationId, Notification::getTaskId)
                .containsExactlyInAnyOrder(
                        tuple(1L, 1L),
                        tuple(3L, 3L)
                );
    }

    @Test
    void findByUserIdReturnsEmptyForUnknownUser() {
        List<Notification> empty = repository.findByUserId(999L);
        assertThat(empty).isEmpty();
    }

    @Test
    void shouldFindByTaskId() {
        Notification a = new Notification(); a.setUserId(1L); a.setTaskId(100L);
        Notification b = new Notification(); b.setUserId(2L); b.setTaskId(200L);
        Notification c = new Notification(); c.setUserId(3L); c.setTaskId(100L);
        repository.save(a); // id=1
        repository.save(b); // id=2
        repository.save(c); // id=3

        List<Notification> task100 = repository.findByTaskId(100L);
        assertThat(task100).hasSize(2)
                .allMatch(n -> n.getTaskId() == 100L)
                .extracting(Notification::getNotificationId, Notification::getUserId)
                .containsExactlyInAnyOrder(
                        tuple(1L, 1L),
                        tuple(3L, 3L)
                );
    }

    @Test
    void findByTaskIdReturnsEmptyForUnknownTask() {
        List<Notification> empty = repository.findByTaskId(12345L);
        assertThat(empty).isEmpty();
    }
}
