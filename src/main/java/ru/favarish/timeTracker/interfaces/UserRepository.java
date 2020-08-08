package ru.favarish.timeTracker.interfaces;

import org.springframework.data.repository.CrudRepository;
import ru.favarish.timeTracker.entities.User;

import java.util.Date;
import java.util.List;

/**
 * Интерфейс для взаимодействия с таблицей пользователей из базы данных
 *
 * @author Fedor Zlobin
 * @version v0.1
 */
public interface UserRepository extends CrudRepository<User, Integer> {
    /**
     * Метод для проверки наличия пользователя с указанным именем в базе
     * @param name имя для проверки
     * @return boolean есть (true) или нет (false) пользователь с указанным именем
     */
    boolean existsByName(String name);

    /**
     * Метод для поиска пользователя по id
     * @param id id искомого пользователя
     * @return User с искомым id
     */
    User findById(int id);

    /**
     * Метод для поиска пользователя по имени
     * @param name имя искомого пользователя
     * @return User с искомым именем
     */
    User findByName(String name);

    /**
     * Метод для поиска всех пользователей
     * @return List всех пользователей из базы данных
     */
    List<User> findAll();
}
