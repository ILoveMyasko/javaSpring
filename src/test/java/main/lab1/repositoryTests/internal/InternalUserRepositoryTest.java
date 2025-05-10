package main.lab1.repositoryTests.internal;
import main.lab1.model.User;
import main.lab1.repos.implementations.internal.InternalUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;

public class InternalUserRepositoryTest {

    private InternalUserRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InternalUserRepository();
    }

    @Test
    void shouldAssignIdsStartingFromOneAndIncrement() {
        User u1 = new User();
        u1.setEmail("a@example.com");
        User saved1 = repository.save(u1);

        User u2 = new User();
        u2.setEmail("b@example.com");
        User saved2 = repository.save(u2);

        assertThat(saved1.getUserId()).isEqualTo(1L);
        assertThat(saved2.getUserId()).isEqualTo(2L);

        assertThat(repository.existsById(1L)).isTrue();
        assertThat(repository.existsById(2L)).isTrue();
    }

    @Test
    void existsByIdReturnsFalseForUnknownId() {
        assertThat(repository.existsById(999L)).isFalse();
    }

    @Test
    void shouldSaveAndFindById() {
        User user = new User();
        user.setEmail("user@example.com");
        User saved = repository.save(user);

        Optional<User> found = repository.findById(saved.getUserId());

        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo("user@example.com");
    }

    @Test
    void findByIdReturnsEmptyOptionalForUnknownId() {
        Optional<User> result = repository.findById(123L);
        assertThat(result).isEmpty();
    }

    @Test
    void findAll_ShouldReturnAllUsers() {
        User u1 = new User(); u1.setEmail("one@example.com");
        User u2 = new User(); u2.setEmail("two@example.com");
        repository.save(u1); // ID = 1
        repository.save(u2); // ID = 2

        List<User> all = repository.findAll();

        assertThat(all).hasSize(2)
                .extracting(User::getUserId, User::getEmail)
                .containsExactlyInAnyOrder(
                        tuple(1L, "one@example.com"),
                        tuple(2L, "two@example.com")
                );
    }

    @Test
    void existsByEmailIsCaseInsensitive() {
        User u = new User();
        u.setEmail("Test@Example.COM");
        repository.save(u);

        assertThat(repository.existsByEmail("test@example.com")).isTrue();
        assertThat(repository.existsByEmail("TEST@example.com")).isTrue();
        assertThat(repository.existsByEmail("nope@example.com")).isFalse();
    }
}
