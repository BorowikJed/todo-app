package eu.kijora.todoapp.controller;

import eu.kijora.todoapp.logic.TaskGroupService;
import eu.kijora.todoapp.model.Task;
import eu.kijora.todoapp.model.TaskRepository;
import eu.kijora.todoapp.model.dto.GroupReadModel;
import eu.kijora.todoapp.model.dto.GroupWriteModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/groups")
public class TaskGroupController {

    public static final Logger logger = LoggerFactory.getLogger((TaskGroupController.class));

    private final TaskGroupService taskGroupService;
    private final TaskRepository taskRepository;

    public TaskGroupController(TaskGroupService taskGroupService, TaskRepository taskRepository) {
        this.taskGroupService = taskGroupService;
        this.taskRepository = taskRepository;
    }

    @GetMapping
    ResponseEntity<List<GroupReadModel>> readAllGroups() {
        logger.warn("Exposing all the tasks!!!");
        return ResponseEntity.ok(taskGroupService.readAll());
    }

    @GetMapping("/{id}")
    ResponseEntity<List<Task>> readAllTasksFromGroup(@PathVariable int id) {
        return ResponseEntity.ok(taskRepository.findAllByGroup_Id(id));
    }

//    @PostMapping
//    ResponseEntity<GroupReadModel> addGroup(@RequestBody @Valid GroupWriteModel groupWriteModel) {
//        return ResponseEntity.ok(taskGroupService.createGroup(groupWriteModel));
//    }
    @PostMapping
    ResponseEntity<GroupReadModel> createGroup(@RequestBody @Valid GroupWriteModel toCreate) {
        GroupReadModel result = taskGroupService.createGroup(toCreate);
        return ResponseEntity.created(URI.create("/" + result.getId())).body(result);
    }

    @Transactional
    @PatchMapping("/{id}")
    public ResponseEntity<?> toggleGroup(@PathVariable int id) {
        taskGroupService.toggleGroup(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<String> handleIllegalArgument(IllegalArgumentException e){
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(IllegalStateException.class)
    ResponseEntity<String> handleIllegalArgument(IllegalStateException e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
