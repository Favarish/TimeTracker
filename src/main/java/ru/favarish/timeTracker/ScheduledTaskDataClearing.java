package ru.favarish.timeTracker;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.favarish.timeTracker.entities.Task;
import ru.favarish.timeTracker.interfaces.TaskRepository;

import java.util.Date;
import java.util.List;

/**
 * Класс задачи по расписанию
 *
 * @author Fedor Zlobin
 * @version v0.1
 */

@Component
public class ScheduledTaskDataClearing {
    /**
     * Связь с таблицей задач в базе данных через Spring Data
     */
    @Autowired
    TaskRepository taskRepository;

    /**
     * Статическая переменная логгера для логировани
     */
    private static final Logger log = Logger.getLogger(ScheduledTaskDataClearing.class);

    /**
     * Переменные для хранения миллисекунд, содержащихся в одной месяце и 12 часах
     */
    private static final long millesecondsMonth = 2592000000L;
    private static final long millesecondsHalfDay = 43200000;

    /**
     * Метод задачи по расписанию которая запускается сама раз в 12 часов
     * для удаления устаревших записей задач
     * (устаревшими считаются записи, завершенные больше месяца назад)
     */
    @Scheduled(fixedRate = millesecondsHalfDay)
    public void dataClearing() {
        log.info("Scheduled task started");
        Date currentDate = new Date();

        List<Task> tasks = taskRepository.findAll();

        for (Task task : tasks) {
            if (task.getTimeFinish() == null) continue;

            if ((currentDate.getTime() - task.getTimeFinish().getTime()) > millesecondsMonth) {
                taskRepository.delete(task);
            }
        }
        log.info("Scheduled task completed successfully");
    }
}
