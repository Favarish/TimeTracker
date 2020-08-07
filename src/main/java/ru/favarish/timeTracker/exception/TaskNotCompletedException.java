package ru.favarish.timeTracker.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class TaskNotCompletedException extends RuntimeException {

    public TaskNotCompletedException(String descriptionTask) {
        super("Предыдущая задача еще не завершена! DescriptionTask: '" + descriptionTask + "'");
    }
}
