package main.lab1.repos.implementations;
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
    private static final AtomicLong idCounter = new AtomicLong();

    @Override
    public boolean existsById(long id) {
        return users.containsKey(id);
    }

    @Override
    public boolean existsByEmail(String email) {
        return users.values()
                    .stream()
                    .anyMatch(user -> user.getEmail().equalsIgnoreCase(email));
    }

    @Override
    public User save(User newUser) {
        newUser.setUserId(idCounter.incrementAndGet());
        return users.put(newUser.getUserId(), newUser);
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
