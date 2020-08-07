package ru.favarish.timeTracker.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NameNotFoundException extends RuntimeException {

    public NameNotFoundException(String name) {
        super("Пользователя с таким именем не существует! Name: '" + name + "'");
    }
}
