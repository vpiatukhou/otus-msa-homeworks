package homework.otusmsahw04.repositories;

import homework.otusmsahw04.entities.User;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface UserRepository extends Repository<User, Long> {

    User save(User entity);

    Optional<User> findById(Long id);

    void deleteById(Long id);

    Iterable<User> findAll();
}
