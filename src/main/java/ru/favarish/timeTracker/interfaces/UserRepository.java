package ru.favarish.timeTracker.interfaces;

import org.springframework.data.repository.CrudRepository;
import ru.favarish.timeTracker.entities.User;

import java.util.Date;
import java.util.List;


public interface UserRepository extends CrudRepository<User, Integer> {
    boolean existsByName(String name);

    User findById(int id);

    User findByNameAndDateBirth(String name, Date dateBirth);

    User findByName(String name);

    List<User> findAll();
}
