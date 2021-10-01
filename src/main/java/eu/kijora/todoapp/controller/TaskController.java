package eu.kijora.todoapp.controller;

import eu.kijora.todoapp.model.Task;
import eu.kijora.todoapp.model.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TaskController {

    public static final Logger logger = LoggerFactory.getLogger((TaskController.class));

    private final TaskRepository taskRepository;

    public TaskController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }


    @GetMapping(path = "/tasks")
    ResponseEntity<List<Task>> readAllTasks(){
        logger.warn("Exposing all the tasks!!!");
        return ResponseEntity.ok(taskRepository.findAll());
    }
}
