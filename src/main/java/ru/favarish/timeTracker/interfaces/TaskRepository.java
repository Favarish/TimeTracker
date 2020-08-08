package ru.favarish.timeTracker.interfaces;

import org.springframework.data.repository.CrudRepository;
import ru.favarish.timeTracker.entities.Task;

import java.util.List;

/**
 * Интерфейс для взаимодействия с таблицей задач из базы данных
 *
 * @author Fedor Zlobin
 * @version v0.1
 */
public interface TaskRepository extends CrudRepository<Task, Integer> {
    /**
     * Метод считывает с базы данных все задачи
     * @return List всех задач из базы данных
     */
    List<Task> findAll();

    /**
     * Метод поиска определенной задачи по id
     * @param id id искомой задачи
     * @return Task объект искомой задачи
     */
    Task findById(int id);

    /**
     * Метод поиска задач по описанию
     * @param descriptionTask описание, по которому будет производиться поиск задач
     * @return List задач с определенным описанием
     */
    List<Task> findByDescriptionTask(String descriptionTask);

    /**
     * Метод поиска задач по id пользователя, выполняющего данные задачи
     * @param userId id пользователя, который выполняет задачи
     * @return List задач, которые выполняет указанный пользователь
     */
    List<Task> findByUserId(int userId);

    /**
     * Метод поиска задач по пользователю, выполяющему задачи, с сортировкой по времени
     * начала выполнения
     * @param userId id пользователя, который выполняет задачи
     * @return List задач, которые выполняет/выполнил пользователь с сортировкой по дате начала выполнения
     */
    List<Task> findByUserIdOrderByTimeStart(Integer userId);

    /**
     * Метод поиска всех задач с сортировкой по времени начала выполнения
     * @return List всех задач, отсортированных по времени начала выполнения
     */
    List<Task> findAllByOrderByTimeStart();
}
