package eu.kijora.todoapp.controller;

import eu.kijora.todoapp.model.Task;
import eu.kijora.todoapp.model.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    public static final Logger logger = LoggerFactory.getLogger((TaskController.class));

    private final TaskRepository taskRepository;

    public TaskController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @GetMapping
    ResponseEntity<List<Task>> readAllTasks() {
        logger.warn("Exposing all the tasks!!!");
        return ResponseEntity.ok(taskRepository.findAll());
    }

    @GetMapping("/{id}")
    ResponseEntity<Task> readTask(@PathVariable int id) {
        return taskRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search/done") //no explicit parameter, but it will be served (optional possibility to request not done)
    ResponseEntity<List<Task>> readDoneTasks(@RequestParam(defaultValue = "true") boolean state) {
        return ResponseEntity.ok(
                taskRepository.findByDone(state)
        );
    }

    @PostMapping
    ResponseEntity<Task> addTask(@RequestBody @Valid Task task) {
        return ResponseEntity.ok(taskRepository.save(task));
    }

    @PutMapping("/{id}") //the other option of saving save() besides @Transactional
    ResponseEntity<?> updateTask(@PathVariable int id, @RequestBody @Valid Task toUpdate) {
        if (!taskRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        } else {
            taskRepository.findById(id)
                    .ifPresent(task -> task.updateFrom(toUpdate));

            taskRepository.save(toUpdate);
            return ResponseEntity.noContent().build();
        }
    }

    @Transactional
    @PatchMapping("/{id}")
    public ResponseEntity<?> toggleTask(@PathVariable int id) {
        if (!taskRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        } else {
            taskRepository.findById(id)
                    .ifPresent(task -> task.setDone(!task.isDone()));
            return ResponseEntity.noContent().build();
        }
    }
}
