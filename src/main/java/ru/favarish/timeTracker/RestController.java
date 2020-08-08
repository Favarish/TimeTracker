package ru.favarish.timeTracker;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.favarish.timeTracker.entities.Task;
import ru.favarish.timeTracker.entities.User;
import ru.favarish.timeTracker.exception.NameExistsException;
import ru.favarish.timeTracker.exception.NameNotFoundException;
import ru.favarish.timeTracker.exception.TaskNotCompletedException;
import ru.favarish.timeTracker.exception.TaskNotFoundException;
import ru.favarish.timeTracker.interfaces.TaskRepository;
import ru.favarish.timeTracker.interfaces.UserRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Класс-контроллер
 *
 * @author Fedor Zlobin
 * version v0.1
 */
@org.springframework.web.bind.annotation.RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class RestController {
    /**
     * Статическая переменная логгера для логирования
     */
    private static final Logger log = Logger.getLogger(RestController.class);

    /**
     * Связь с таблицей пользователей в базе данных через Spring Data
     */
    @Autowired
    UserRepository userRepository;

    /**
     * Связь с таблицей задач в базе данных через Spring Data
     */
    @Autowired
    TaskRepository taskRepository;

    /**
     * Переменные для форматирования даты из строки и обратно
     */
    SimpleDateFormat formatterDateBirth = new SimpleDateFormat("dd.M.yyyy");
    SimpleDateFormat formatterTimeTask = new SimpleDateFormat("HH:mm dd.M.yyyy");
    SimpleDateFormat formatterHHmm = new SimpleDateFormat("HH:mm");
    SimpleDateFormat formatterDdHHmm = new SimpleDateFormat("dd HH:mm");

    /**
     * Метод для создания нового пользователя по имени
     *
     * @param name имя для нового пользователя
     */
    @PutMapping("create_user/{name}")
    public void createUser(@PathVariable("name") String name) {
        User user = userRepository.findByName(name);
        if (user != null) {
            log.error("Request: create_user/" + name + "; Reason: user already exists");
            throw new NameExistsException(name);
        }
        user = new User();
        user.setName(name);

        userRepository.save(user);
        log.info("New user created: Name: " + name);
    }

    /**
     * Метод для создания нового пользователя по имени и дате рождения
     *
     * @param name имя для нового пользователя
     * @param dateBirthStr дата рождения для нового пользователя
     * @throws ParseException исключение выбросится в случае неправильного ввода даты
     */
    @PutMapping("/create_user/{name}/{dateBirthStr}")
    public void createUser(@PathVariable("name") String name,
                           @PathVariable("dateBirthStr") String dateBirthStr) throws ParseException {
        User user = userRepository.findByName(name);
        if (user != null) {
            log.error("Request: create_user/" + name + "/" + dateBirthStr + "; Reason: user already exists");
            throw new NameExistsException(name);
        }
        Date dateBirth = formatterDateBirth.parse(dateBirthStr);
        user = new User();
        user.setName(name);
        user.setDateBirth(dateBirth);

        userRepository.save(user);
        log.info("New user created: Name: " + name);
    }

    /**
     * Метод для создания нового пользователя с описанием по имени, дате рождения
     *
     * @param name имя для нового пользователя
     * @param dateBirthStr дата рождения для нового пользователя
     * @param description описания для нового пользователя
     * @throws ParseException исключение выбросится в случае неправильного ввода даты
     */
    @PutMapping("/create_user/{name}/{dateBirthStr}/{description}")
    public void createUser(@PathVariable("name") String name,
                           @PathVariable("dateBirthStr") String dateBirthStr,
                           @PathVariable("description") String description) throws ParseException {
        User user = userRepository.findByName(name);
        if (user != null) {
            log.error("Request: create_user/" + name + "/" + dateBirthStr + "/"
                    + description + "; Reason: user already exists");
            throw new NameExistsException(name);
        }
        Date dateBirth = formatterDateBirth.parse(dateBirthStr);
        user = new User(name, dateBirth, description);

        userRepository.save(user);
        log.info("New user created: Name: " + name);
    }

    /**
     * Метод для изменения имени пользователя
     *
     * @param name старое имя пользователя
     * @param newName новое имя для пользователя
     */
    @PostMapping("user_update_name/{name}/{newName}")
    public void userUpdateName(@PathVariable("name") String name,
                               @PathVariable("newName") String newName) {
        if (!userRepository.existsByName(name)) {
            log.error("Request: user_update_name/" + name + "/" + newName + "; " +
                    "Reason: user with this name does not exist");
            throw new NameExistsException(name);
        }
        User user = userRepository.findByName(name);
        user.setName(newName);

        userRepository.save(user);
        log.info("User data changed: Old name: " + name + "; New name: " + newName);
    }

    /**
     * Метод изменения/добавления информации о дате рождения пользователя
     *
     * @param name имя пользователя
     * @param dateBirthStr новая дата рождения для пользователя
     * @throws ParseException исключение выбросится в случае неправильного ввода даты
     */
    @PostMapping("user_update_dateBirth/{name}/{dateBirthStr}")
    public void userUpdateDateBirth(@PathVariable("name") String name,
                                    @PathVariable("dateBirthStr") String dateBirthStr) throws ParseException {
        if (!userRepository.existsByName(name)) {
            log.error("Request: user_update_dateBirth/" + name + "/" + dateBirthStr + "; " +
                    "Reason: user with this name does not exist");
            throw new NameExistsException(name);
        }
        User user = userRepository.findByName(name);
        Date date = formatterDateBirth.parse(dateBirthStr);
        user.setDateBirth(date);

        userRepository.save(user);
        log.info("User data changed: Name: " + name + "; New data birth: " + dateBirthStr);
    }

    /**
     * Метод для изменения/добавления описания пользователя
     *
     * @param name имя пользователя, у которого хотим изменить/добавить описание
     * @param description новое описание
     */
    @PostMapping("user_update_description/{name}/{description}")
    public void userUpdateDescription(@PathVariable("name") String name,
                                      @PathVariable("description") String description) {
        if (!userRepository.existsByName(name)) {
            log.error("Request: user_update_description/" + name + "/" + description + "; " +
                    "Reason: user with this name does not exist");
            throw new NameExistsException(name);
        }
        User user = userRepository.findByName(name);
        user.setDescription(description);

        userRepository.save(user);
        log.info("User data changed; Name: " + name + "; New description: " + description);
    }

    /**
     * Метод для изменения/добавления информации о пользователе
     *
     * @param name имя пользователя, у которого хотим изменить/добавить дату рождения и описание
     * @param dateBirthStr новая дата рождения
     * @param description новое описание
     * @throws ParseException исключение выбросится в случае неправильного ввода даты
     */
    @PostMapping("user_update_all/{name}/{dateBirthStr}/{description}")
    public void userUpdateAll(@PathVariable("name") String name,
                              @PathVariable("dateBirthStr") String dateBirthStr,
                              @PathVariable("description") String description) throws ParseException {
        if (!userRepository.existsByName(name)) {
            log.error("Request: user_update_all/" + name + "/" + dateBirthStr + "/" + description + "; " +
                    "Reason: user with this name does not exist");
            throw new NameExistsException(name);
        }
        User user = userRepository.findByName(name);
        Date date = formatterDateBirth.parse(dateBirthStr);
        user.setDateBirth(date);
        user.setDescription(description);

        userRepository.save(user);
        log.info("User data changed: Name: " + name + "; New date birth: " + dateBirthStr + "; " +
                "New description: " + description);
    }

    /**
     * Метод для изменения всей информации о пользователе включая его текущее имя
     *
     * @param name старое имя пользователя
     * @param newName новое имя пользователя
     * @param dateBirthStr новая дата рождения
     * @param description новое описание
     * @throws ParseException исключение выбросится в случае неправильного ввода даты
     */
    @PostMapping("user_update_all/{name}/{newName}/{dateBirthStr}/{description}")
    public void userUpdateAll(@PathVariable("name") String name,
                              @PathVariable("newName") String newName,
                              @PathVariable("dateBirthStr") String dateBirthStr,
                              @PathVariable("description") String description) throws ParseException {
        if (!userRepository.existsByName(name)) {
            log.error("Request: user_update_all/" + name + "/" + newName + "/" + dateBirthStr + "/"
                    + description + "; " + "Reason: user with this name does not exist");
            throw new NameExistsException(name);
        }
        User user = userRepository.findByName(name);
        Date date = formatterDateBirth.parse(dateBirthStr);
        user.setName(newName);
        user.setDateBirth(date);
        user.setDescription(description);

        userRepository.save(user);
        log.info("User data changed: Name: " + name + "; New name:" + newName + "; " +
                "New date birth: " + dateBirthStr + "; " + "New description: " + description);
    }

    /**
     * Метод для добавления новой задачи оперделенному пользователю
     *
     * @param userName пользователь, которому добавляется задача
     * @param descriptionTask описание добавляемой задачи
     */
    @PutMapping("create_task/{userName}/{descriptionTask}")
    public void createTask(@PathVariable("userName") String userName,
                           @PathVariable("descriptionTask") String descriptionTask){
        if (!userRepository.existsByName(userName)) {
            log.error("Request: create_task/" + userName + "/" + descriptionTask + "; " +
                    "Reason: user with this name does not exist");
            throw new NameNotFoundException(userName);
        }
        List<Task> tasksCheck = taskRepository.findByDescriptionTask(descriptionTask);
        for (Task task : tasksCheck) {
            if (task.getTimeFinish() == null) {
                throw new TaskNotCompletedException(descriptionTask);
            }
        }

        Date dateStart = new Date();
        User user = userRepository.findByName(userName);
        Task task = new Task(user.getId(), descriptionTask, dateStart);

        taskRepository.save(task);
        log.info("New task created: For user: " + userName + "; description: " + descriptionTask);
    }

    /**
     * Метод для завершения конкретной задачи у конкретного пользователя
     *
     * @param userName имя пользователя, у которого хотим завершить задачу
     * @param descriptionTask описание задачи
     */
    @PostMapping("complete_task/{userName}/{descriptionTask}")
    public void completeTask(@PathVariable("userName") String userName,
                             @PathVariable("descriptionTask") String descriptionTask) {
        if (!userRepository.existsByName(userName)) {
            log.error("Request: complete_task/" + userName + "/" + descriptionTask + "; " +
                    "Reason: user with this name does not exist");
            throw new NameNotFoundException(userName);
        }
        List<Task> tasks = taskRepository.findByDescriptionTask(descriptionTask);
        Task task = null;
        if (tasks == null) {
            throw new TaskNotFoundException(descriptionTask);
        } else {
            boolean check = false;
            for (Task taskCheck : tasks) {
                if (taskCheck.getTimeFinish() == null) {
                    check = true;
                    task = taskCheck;
                    break;
                }
            }
            if (!check) {
                throw new TaskNotFoundException(descriptionTask);
            }
        }
        Date date = new Date();
        task.setTimeFinish(date);

        taskRepository.save(task);
        log.info("Task completed successfully: For user: " + userName + "; description: " + descriptionTask);
    }

    /**
     * Метод для вывода трудозатрат за конкретный промежуток времени пользователя
     * в виде связного списка 'Задача - Сумма затраченного времени'
     *
     * @param userName имя пользователя, у которого будут выведены трудозатраты
     * @param dateAStr время начала периода для анализа
     * @param dateBStr время конца периода для анализа
     * @return Map задач у пользователя с временем выполнения каждой задачи
     * @throws ParseException исключение выбросится в случае неправильного ввода даты
     */
    @GetMapping("show_work_costs/{userName}/{dateA}/{dateB}")
    public Map<String, String> showWorkCosts(@PathVariable("userName") String userName,
                                             @PathVariable("dateA") String dateAStr,
                                             @PathVariable("dateB") String dateBStr) throws ParseException {
        User user = userRepository.findByName(userName);
        if (user == null) {
            log.error("Request: show_work_costs/" + userName + "/" + dateAStr + "/" + dateBStr + "; " +
                    "Reason: user with this name does not exist");
            throw new NameNotFoundException(userName);
        }
        Date dateA = formatterTimeTask.parse(dateAStr);
        Date dateB = formatterTimeTask.parse(dateBStr);
        List<Task> tasks = taskRepository.findByUserId(user.getId());
        List<Task> needTask = new ArrayList<>();
        for (Task task : tasks) {
            if (task.getTimeFinish() == null) {
                continue;
            }
            if ((dateA.before(task.getTimeStart())) && (dateB.after(task.getTimeFinish()))) {
                needTask.add(task);
            }
        }

        Map<String, String> taskTime = new HashMap<String, String>();

        for (Task task : needTask) {
            long timeMillies = Math.abs(task.getTimeStart().getTime() - task.getTimeFinish().getTime());
            long timeMinutes = TimeUnit.MINUTES.convert(timeMillies, TimeUnit.MILLISECONDS);
            long hours = timeMinutes / 60;
            long minutes = timeMinutes % 60;
            String time = "" + hours + ":" + minutes;

            taskTime.put(task.getDescriptionTask(), time);
        }

        return taskTime;
    }

    /**
     * Метод для вывода временных интервалов, занятых работой у всех пользователей
     *
     * @param timeAStr время начала интересующего периода
     * @param timeBStr время конца интересующего периода
     * @return Map задач и временных интервалов, потраченных на их выполнение (незавершенные задачи не учитываются)
     * @throws ParseException исключение выбросится в случае неправильного ввода даты
     */
    @GetMapping("show_time_intervals/{timeA}/{timeB}")
    public Map<String, String> showTimeIntervals(@PathVariable("timeA") String timeAStr,
                                                 @PathVariable("timeB") String timeBStr) throws ParseException {
        Date dateA = formatterTimeTask.parse(timeAStr);
        Date dateB = formatterTimeTask.parse(timeBStr);
        List<Task> tasks = taskRepository.findAllByOrderByTimeStart();
        List<Task> needTask = new ArrayList<>();
        for (Task task : tasks) {
            if (task.getTimeFinish() == null) {
                continue;
            }
            if ((dateA.before(task.getTimeStart())) && (dateB.after(task.getTimeFinish()))) {
                needTask.add(task);
            }
        }

        Map<String, String> taskInterval = new HashMap<String, String>();

        for (Task task : needTask) {

            taskInterval.put(task.getDescriptionTask(), "" + formatterDdHHmm.format(task.getTimeStart()) +
                    " - " + formatterDdHHmm.format(task.getTimeFinish()));
        }

        return taskInterval;
    }

    /**
     * Метод для вывода суммы трудозатрат конкретного пользователя за указанный период времени
     *
     * @param userName имя пользователя, для которого будет посчитанна сумма трудозатрат
     * @param dateAStr начало интересующего периода
     * @param dateBStr конец интересующего периода
     * @return String имя пользователя и сумма его трудозатрат в формате HH:mm
     * @throws ParseException исключение выбросится в случае неправильного ввода даты
     */
    @GetMapping("show_sum_workcosts/{userName}/{dateA}/{dateB}")
    public String showSumWorkCosts(@PathVariable("userName") String userName,
                                   @PathVariable("dateA") String dateAStr,
                                   @PathVariable("dateB") String dateBStr) throws ParseException {
        User user = userRepository.findByName(userName);
        if (user == null) {
            log.error("Request: show_sum_workcosts/" + userName + "/" + dateAStr + "/" + dateBStr + "; " +
                    "Reason: user with this name does not exist");
            throw new NameNotFoundException(userName);
        }
        Date dateA = formatterTimeTask.parse(dateAStr);
        Date dateB = formatterTimeTask.parse(dateBStr);
        List<Task> tasks = taskRepository.findByUserId(user.getId());
        List<Task> needTask = new ArrayList<>();
        for (Task task : tasks) {
            if (task.getTimeFinish() == null) {
                continue;
            }
            if ((dateA.before(task.getTimeStart())) && (dateB.after(task.getTimeFinish()))) {
                needTask.add(task);
            }
        }

        long timeMillies = 0;
        for (Task task : needTask) {
            timeMillies += Math.abs(task.getTimeStart().getTime() - task.getTimeFinish().getTime());
        }
        long timeMinutes = TimeUnit.MINUTES.convert(timeMillies, TimeUnit.MILLISECONDS);
        long hours = timeMinutes / 60;
        long minutes = timeMinutes % 60;
        String time = "" + hours + ":" + minutes;
        return userName + " - " + time;
    }

    /**
     * Метод для удаления всей информации о пользователе
     *
     * @param userName имя пользователя, которого нужно удалить из базы данных
     */
    @DeleteMapping("delete_user/{userName}")
    public void deleteUserInfo(@PathVariable("userName") String userName) {
        User user = userRepository.findByName(userName);
        if (user == null) {
            log.error("Request: delete_user_info/" + userName + "; " + "Reason: user with this name does not exist");
            throw new NameNotFoundException(userName);
        }
        userRepository.delete(user);
        log.info("User information deleted: User name:" + userName);
    }

    /**
     * Метод для очистки всех данных трекинга у конкретного пользователя
     *
     * @param userName имя пользователя, у которого нужно удалить все задачи
     */
    @DeleteMapping("clear_tracking_data/{userName}")
    public void clearTrackingData(@PathVariable("userName") String userName) {
        User user = userRepository.findByName(userName);
        if (user == null) {
            log.error("Request: clear_tracking_data/" + userName + "; " + "Reason: user with this name does not exist");
            throw new NameNotFoundException(userName);
        }
        List<Task> tasks = taskRepository.findByUserId(user.getId());
        for (Task task : tasks) {
            taskRepository.delete(task);
        }
        log.info("Tracking data user '" + userName + "' was  deleted");
    }
}
