package ru.favarish.timeTracker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.favarish.timeTracker.entities.User;
import ru.favarish.timeTracker.exception.NameExistsException;
import ru.favarish.timeTracker.interfaces.TaskRepository;
import ru.favarish.timeTracker.interfaces.UserRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@org.springframework.web.bind.annotation.RestController
public class RestController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    TaskRepository taskRepository;


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
        SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy");
        Date dateBirth = formatter.parse(dateBirthStr);
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
        SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy");
        Date dateBirth = formatter.parse(dateBirthStr);
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
        SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy");
        Date date = formatter.parse(dateBirthStr);
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
        SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy");
        Date date = formatter.parse(dateBirthStr);
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
        SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy");
        Date date = formatter.parse(dateBirthStr);
        user.setName(newName);
        user.setDateBirth(date);
        user.setDescription(description);

        userRepository.save(user);
    }



}
