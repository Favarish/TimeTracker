package ru.favarish.timeTracker.interfaces;

import org.springframework.data.repository.CrudRepository;
import ru.favarish.timeTracker.entities.User;


public interface UserRepository extends CrudRepository<User, Integer> {
}
