package ru.favarish.timeTracker;

import org.springframework.beans.factory.annotation.Autowired;
import ru.favarish.timeTracker.interfaces.TaskRepository;
import ru.favarish.timeTracker.interfaces.UserRepository;

@org.springframework.web.bind.annotation.RestController
public class RestController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    TaskRepository taskRepository;

    
}
