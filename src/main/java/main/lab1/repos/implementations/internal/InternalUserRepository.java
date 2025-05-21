package main.lab1.repos.implementations.internal;
import main.lab1.repos.UserRepository;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import main.lab1.model.User;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

@Repository
@Profile("internal")
public class InternalUserRepository implements UserRepository {

    private final Map<Long, User> users = new HashMap<>(); // can skip <> params since java 7
    private final AtomicLong idCounter = new AtomicLong();

    @Override
    public boolean existsById(long id) {
        return users.containsKey(id);
    }

    @Override
    public boolean existsByEmailIgnoreCase(String email) {
        return users.values()
                    .stream()
                    .anyMatch(user -> user.getEmail().equalsIgnoreCase(email));
    }

    @Override
    public User save(User newUser) {
        newUser.setUserId(idCounter.incrementAndGet());
        users.put(newUser.getUserId(), newUser);
        return newUser;
    }

    @Override
    public Optional<User> findById(long id) {
       return Optional.ofNullable(users.get(id));
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }
}
