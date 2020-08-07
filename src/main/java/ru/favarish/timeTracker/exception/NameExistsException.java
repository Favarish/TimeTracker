package ru.favarish.timeTracker.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class NameExistsException extends RuntimeException {

    public NameExistsException(String name) {
        super("Пользователь с таким именем уже существует! Name = '" + name + "'");
    }
}
