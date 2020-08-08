package ru.favarish.timeTracker.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Класс исключения, выбрасываемого при попытке создать нового пользователя
 * с существующем (в базе данных) именем
 *
 * @author Fedor Zlobin
 * @version v0.1
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class NameExistsException extends RuntimeException {

    public NameExistsException(String name) {
        super("Пользователь с таким именем уже существует! Name = '" + name + "'");
    }
}
