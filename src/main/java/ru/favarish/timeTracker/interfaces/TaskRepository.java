package ru.favarish.timeTracker.interfaces;

import org.springframework.data.repository.CrudRepository;
import ru.favarish.timeTracker.entities.Task;

import java.util.List;

public interface TaskRepository extends CrudRepository<Task, Integer> {
    List<Task> findAll();

    Task findById(int id);

    Task findByName(String name);

    List<Task> findByUserId(int userId);

    List<Task> findByUserIdOrderByTimeStart(Integer userId);
}
