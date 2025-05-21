package main.lab1.repositoryTests.internal;
import main.lab1.model.Task;
import main.lab1.repos.implementations.internal.InternalTaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class InternalTaskRepositoryTest {
    private InternalTaskRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InternalTaskRepository();
    }

    @Test
    void shouldAssignIdsStartingFromOneAndIncrement() {
        Task t1 = new Task();
        Task saved1 = repository.save(t1);

        Task t2 = new Task();
        Task saved2 = repository.save(t2);

        assertThat(saved1.getTaskId()).isEqualTo(1L);
        assertThat(saved2.getTaskId()).isEqualTo(2L);

        assertThat(repository.existsById(1L)).isTrue();
        assertThat(repository.existsById(2L)).isTrue();
    }

    @Test
    void shouldSaveAndFindById() {
        Task task = new Task();
        task.setUserId(10L);
        Task saved = repository.save(task);

        Optional<Task> found = repository.findById(saved.getTaskId());
        assertThat(found).isPresent();
        assertThat(found.get().getUserId()).isEqualTo(10L);
    }

    @Test
    void existsById_ReturnsFalseForUnknownId() {
        assertThat(repository.existsById(999L)).isFalse();
    }

    @Test
    void findById_ReturnsEmptyOptionalForUnknownId() {
        Optional<Task> result = repository.findById(123L);
        assertThat(result).isEmpty();
    }

    @Test
    void findAll_shouldReturnAllTasks() {
        repository.save(new Task());
        repository.save(new Task());
        repository.save(new Task());

        List<Task> all = repository.findAll();
        assertThat(all).hasSize(3)
                .extracting(Task::getTaskId)
                .containsExactlyInAnyOrder(1L, 2L, 3L);
    }

    @Test
    void findByUserId_shouldFindAllUserTasks() {
        Task task1 = new Task(); //id = 1
        task1.setUserId(100L);
        Task task2 = new Task(); //id = 2
        task2.setUserId(200L);
        Task task3 = new Task(); //id = 3
        task3.setUserId(100L);

        repository.save(task1);
        repository.save(task2);
        repository.save(task3);

        List<Task> user100 = repository.findByUserId(100L);
        assertThat(user100).hasSize(2)
                .allMatch(t -> t.getUserId() == 100L)
                .extracting(Task::getTaskId)
                .containsExactlyInAnyOrder(1L, 3L);
    }

    @Test
    void deleteById_shouldDeleteById() {
        Task task = new Task();
        task.setUserId(7L);
        Task saved = repository.save(task); // id = 1

        assertThat(repository.existsById(1L)).isTrue();
        repository.deleteById(1L);
        assertThat(repository.existsById(1L)).isFalse();
        assertThat(repository.findById(1L)).isEmpty();
    }

    @Test
    void findByUserIdAndIsCompletedFalse_ShouldReturnOnlyWithCompletedFalse()
    {
        Task task1 = new Task(); //id = 1
        task1.setUserId(100L);
        Task task2 = new Task(); //id = 2
        task2.setUserId(200L);
        Task task3 = new Task(); //id = 3
        task3.setUserId(100L);
        task3.setCompleted(true);

        repository.save(task1);
        repository.save(task2);
        repository.save(task3);

        List<Task> retrievedTasks = repository.findByUserIdAndIsCompletedFalse(100L);

        assertEquals(1, retrievedTasks.size());
        assertTrue(retrievedTasks.contains(task1));
    }


}
//  findByIsCompletedTrueAndExpiresAtAfter