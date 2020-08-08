package ru.favarish.timeTracker.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Класс исключения, выбрасываемого при попытке создать новую задачу
 * с именем незавершенной старой
 *
 * @author Fedor Zlobin
 * @version v0.1
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class TaskNotCompletedException extends RuntimeException {

    public TaskNotCompletedException(String descriptionTask) {
        super("Предыдущая задача еще не завершена! DescriptionTask: '" + descriptionTask + "'");
    }
}
