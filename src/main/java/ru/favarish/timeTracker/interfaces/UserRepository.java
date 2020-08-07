package ru.favarish.timeTracker.interfaces;

import org.springframework.data.repository.CrudRepository;
import ru.favarish.timeTracker.entities.User;

import java.util.List;


public interface UserRepository extends CrudRepository<User, Integer> {
    boolean existsByName(String name);

    User findById(int id);

    User findByName(String name);

    List<User> findAll();
}
