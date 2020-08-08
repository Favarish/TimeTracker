package ru.favarish.timeTracker;

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

@org.springframework.web.bind.annotation.RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class RestController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    TaskRepository taskRepository;
    SimpleDateFormat formatterDateBirth = new SimpleDateFormat("dd.M.yyyy");
    SimpleDateFormat formatterTimeTask = new SimpleDateFormat("HH:mm dd.M.yyyy");
    SimpleDateFormat formatterHHmm = new SimpleDateFormat("HH:mm");
    SimpleDateFormat formatterDdHHmm = new SimpleDateFormat("dd HH:mm");

    @PutMapping("create_user/{name}")
    public void createUser(@PathVariable("name") String name) {
        User user = userRepository.findByName(name);
        if (user != null) {
            throw new NameExistsException(name);
        }
        user = new User();
        user.setName(name);

        userRepository.save(user);
    }

    @PutMapping("/create_user/{name}/{dateBirthStr}")
    public void createUser(@PathVariable("name") String name,
                           @PathVariable("dateBirthStr") String dateBirthStr) throws ParseException {
        User user = userRepository.findByName(name);
        if (user != null) {
            throw new NameExistsException(name);
        }
        Date dateBirth = formatterDateBirth.parse(dateBirthStr);
        user = new User();
        user.setName(name);
        user.setDateBirth(dateBirth);

        userRepository.save(user);
    }

    @PutMapping("/create_user/{name}/{dateBirthStr}/{description}")
    public void createUser(@PathVariable("name") String name,
                           @PathVariable("dateBirthStr") String dateBirthStr,
                           @PathVariable("description") String description) throws ParseException {
        User user = userRepository.findByName(name);
        if (user != null) {
            throw new NameExistsException(name);
        }
        Date dateBirth = formatterDateBirth.parse(dateBirthStr);
        user = new User(name, dateBirth, description);

        userRepository.save(user);
    }

    @PostMapping("user_update_name/{name}/{newName}")
    public void userUpdateName(@PathVariable("name") String name,
                               @PathVariable("newName") String newName) {
        if (!userRepository.existsByName(name)) {
            throw new NameExistsException(name);
        }
        User user = userRepository.findByName(name);
        user.setName(newName);

        userRepository.save(user);
    }

    @PostMapping("user_update_dateBirth/{name}/{dateBirthStr}")
    public void userUpdateDateBirth(@PathVariable("name") String name,
                                    @PathVariable("dateBirthStr") String dateBirthStr) throws ParseException {
        if (!userRepository.existsByName(name)) {
            throw new NameExistsException(name);
        }
        User user = userRepository.findByName(name);
        Date date = formatterDateBirth.parse(dateBirthStr);
        user.setDateBirth(date);

        userRepository.save(user);
    }

    @PostMapping("user_update_description/{name}/{description}")
    public void userUpdateDescription(@PathVariable("name") String name,
                                      @PathVariable("description") String description) {
        if (!userRepository.existsByName(name)) {
            throw new NameExistsException(name);
        }
        User user = userRepository.findByName(name);
        user.setDescription(description);

        userRepository.save(user);
    }

    @PostMapping("user_update_all/{name}/{dateBirthStr}/{description}")
    public void userUpdateAll(@PathVariable("name") String name,
                              @PathVariable("dateBirthStr") String dateBirthStr,
                              @PathVariable("description") String description) throws ParseException {
        if (!userRepository.existsByName(name)) {
            throw new NameExistsException(name);
        }
        User user = userRepository.findByName(name);
        Date date = formatterDateBirth.parse(dateBirthStr);
        user.setDateBirth(date);
        user.setDescription(description);

        userRepository.save(user);
    }

    @PostMapping("user_update_all/{name}/{newName}/{dateBirthStr}/{description}")
    public void userUpdateAll(@PathVariable("name") String name,
                              @PathVariable("newName") String newName,
                              @PathVariable("dateBirthStr") String dateBirthStr,
                              @PathVariable("description") String description) throws ParseException {
        if (!userRepository.existsByName(name)) {
            throw new NameExistsException(name);
        }
        User user = userRepository.findByName(name);
        Date date = formatterDateBirth.parse(dateBirthStr);
        user.setName(newName);
        user.setDateBirth(date);
        user.setDescription(description);

        userRepository.save(user);
    }

    @PutMapping("create_task/{userName}/{descriptionTask}")
    public void createTask(@PathVariable("userName") String userName,
                           @PathVariable("descriptionTask") String descriptionTask) throws ParseException {
        if (!userRepository.existsByName(userName)) {
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
    }

    @PostMapping("complete_task/{userName}/{descriptionTask}")
    public void completeTask(@PathVariable("userName") String userName,
                             @PathVariable("descriptionTask") String descriptionTask) {
        if (!userRepository.existsByName(userName)) {
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
    }

    @GetMapping("show_work_costs/{userName}/{dateA}/{dateB}")
    public Map<String, String> showWorkCosts(@PathVariable("userName") String userName,
                                             @PathVariable("dateA") String dateAStr,
                                             @PathVariable("dateB") String dateBStr) throws ParseException {
        User user = userRepository.findByName(userName);
        if (user == null) {
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

    @GetMapping("show_sum_workcosts/{userName}/{dateA}/{dateB}")
    public String showSumWorkCosts(@PathVariable("userName") String userName,
                                   @PathVariable("dateA") String dateAStr,
                                   @PathVariable("dateB") String dateBStr) throws ParseException {
        User user = userRepository.findByName(userName);
        if (user == null) {
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

    @DeleteMapping("delete_user_info/{userName}")
    public void deleteUserInfo(@PathVariable("userName") String userName) {
        User user = userRepository.findByName(userName);
        if (user == null) {
            throw new NameNotFoundException(userName);
        }
        user.setDescription(null);
        user.setDateBirth(null);
        userRepository.save(user);
    }

    @DeleteMapping("clear_tracking_data/{userName}")
    public void clearTrackingData(@PathVariable("userName") String userName) {
        User user = userRepository.findByName(userName);
        if (user == null) {
            throw new NameNotFoundException(userName);
        }
        List<Task> tasks = taskRepository.findByUserId(user.getId());
        for (Task task : tasks) {
            taskRepository.delete(task);
        }
    }
}
