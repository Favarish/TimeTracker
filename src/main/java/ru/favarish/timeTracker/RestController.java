package ru.favarish.timeTracker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
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
import java.util.Date;
import java.util.List;

@org.springframework.web.bind.annotation.RestController
public class RestController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    TaskRepository taskRepository;
    SimpleDateFormat formatterDateBirth = new SimpleDateFormat("dd.M.yyyy");
    SimpleDateFormat formatterTimeTask = new SimpleDateFormat("HH:mm dd.M.yyyy");

    @RequestMapping("create_user/{name}")
    public void createUser(@PathVariable("name") String name) {
        User user = userRepository.findByName(name);
        if (user != null) {
            throw new NameExistsException(name);
        }
        user = new User();
        user.setName(name);

        userRepository.save(user);
    }

    @RequestMapping("/create_user/{name}/{dateBirthStr}")
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

    @RequestMapping("/create_user/{name}/{dateBirthStr}/{description}")
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

    @RequestMapping("user_update_name/{name}/{newName}")
    public void userUpdateName(@PathVariable("name") String name,
                               @PathVariable("newName") String newName) {
        if (!userRepository.existsByName(name)) {
            throw new NameExistsException(name);
        }
        User user = userRepository.findByName(name);
        user.setName(newName);

        userRepository.save(user);
    }

    @RequestMapping("user_update_dateBirth/{name}/{dateBirthStr}")
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

    @RequestMapping("user_update_description/{name}/{description}")
    public void userUpdateDescription(@PathVariable("name") String name,
                                      @PathVariable("description") String description) {
        if (!userRepository.existsByName(name)) {
            throw new NameExistsException(name);
        }
        User user = userRepository.findByName(name);
        user.setDescription(description);

        userRepository.save(user);
    }

    @RequestMapping("user_update_all/{name}/{dateBirthStr}/{description}")
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

    @RequestMapping("user_update_all/{name}/{newName}/{dateBirthStr}/{description}")
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

    @RequestMapping("create_task/{userName}/{descriptionTask}")
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

    @RequestMapping("create_task/{userName}/{descriptionTask}/{timeStart}")
    public void createTask(@PathVariable("userName") String userName,
                           @PathVariable("descriptionTask") String descriptionTask,
                           @PathVariable("timeStart") String timeStart) throws ParseException {
        if (!userRepository.existsByName(userName)) {
            throw new NameNotFoundException(userName);
        }
        List<Task> tasksCheck = taskRepository.findByDescriptionTask(descriptionTask);
        for (Task task : tasksCheck) {
            if (task.getTimeFinish() == null) {
                throw new TaskNotCompletedException(descriptionTask);
            }
        }

        Date dateStart = formatterTimeTask.parse(timeStart);
        User user = userRepository.findByName(userName);
        Task task = new Task(user.getId(), descriptionTask, dateStart);

        taskRepository.save(task);
    }

    @RequestMapping("complete_task/{userName}/{descriptionTask}")
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


}
