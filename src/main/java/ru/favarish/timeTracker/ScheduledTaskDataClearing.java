package ru.favarish.timeTracker;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.favarish.timeTracker.entities.Task;
import ru.favarish.timeTracker.interfaces.TaskRepository;

import java.util.Date;
import java.util.List;

@Component
public class ScheduledTaskDataClearing {
    @Autowired
    TaskRepository taskRepository;

    private static final Logger log = Logger.getLogger(ScheduledTaskDataClearing.class);

    private static final long millesecondsMonth = 2592000000L;
    private static final long millesecondsHalfDay = 43200000;

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
