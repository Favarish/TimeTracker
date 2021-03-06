package ru.favarish.timeTracker.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Класс исключения, выбрасываемого при попытке завершить несуществующую задачу
 *
 * @author Fedor Zlobin
 * @version v0.1
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class TaskNotFoundException extends RuntimeException {

    public TaskNotFoundException(String description) {
        super("Задачи с таким описанием не существует! Description: '" + description + "'");
    }
}
