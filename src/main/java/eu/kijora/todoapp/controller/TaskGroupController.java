package eu.kijora.todoapp.controller;

import eu.kijora.todoapp.logic.TaskGroupService;
import eu.kijora.todoapp.model.Task;
import eu.kijora.todoapp.model.TaskRepository;
import eu.kijora.todoapp.model.dto.GroupReadModel;
import eu.kijora.todoapp.model.dto.GroupTaskWriteModel;
import eu.kijora.todoapp.model.dto.GroupWriteModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@Controller
@IllegalExceptionProcessing
@RequestMapping("/groups")
public class TaskGroupController {

    public static final Logger logger = LoggerFactory.getLogger((TaskGroupController.class));

    private final TaskGroupService taskGroupService;
    private final TaskRepository taskRepository;

    TaskGroupController(TaskGroupService taskGroupService, TaskRepository taskRepository) {
        this.taskGroupService = taskGroupService;
        this.taskRepository = taskRepository;
    }

    @GetMapping(produces = MediaType.TEXT_HTML_VALUE) //the one for Thymeleaf
    String showGroups(Model model){
        model.addAttribute("group", new GroupWriteModel());
        return "groups";
    }

    @PostMapping(params = "addTask", produces = MediaType.TEXT_HTML_VALUE)
    String addGroupTask(@ModelAttribute("group") GroupWriteModel current){ //How does it know it's THE projectWriteModel?
        current.getTasks().add(new GroupTaskWriteModel());
        return "groups";
    }

    @PostMapping(produces = MediaType.TEXT_HTML_VALUE, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    String addGroup(@ModelAttribute("group") @Valid GroupWriteModel current,
                      BindingResult bindingResult,
                      Model model){
        if(bindingResult.hasErrors()){
            return "groups";
        }
        taskGroupService.createGroup(current);
        model.addAttribute("group", new GroupWriteModel());
        model.addAttribute("groups", getGroups());
        model.addAttribute("message", "Group added");
        return "groups";

    }


    @ResponseBody
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<GroupReadModel>> readAllGroups() {
        logger.warn("Exposing all the tasks!!!");
        return ResponseEntity.ok(taskGroupService.readAll());
    }

    @ResponseBody
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<Task>> readAllTasksFromGroup(@PathVariable int id) {
        return ResponseEntity.ok(taskRepository.findAllByGroup_Id(id));
    }

    //    @PostMapping
//    ResponseEntity<GroupReadModel> addGroup(@RequestBody @Valid GroupWriteModel groupWriteModel) {
//        return ResponseEntity.ok(taskGroupService.createGroup(groupWriteModel));
//    }
    @ResponseBody
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<GroupReadModel> createGroup(@RequestBody @Valid GroupWriteModel toCreate) {
        GroupReadModel result = taskGroupService.createGroup(toCreate);
        return ResponseEntity.created(URI.create("/" + result.getId())).body(result);
    }

    @ResponseBody
    @Transactional
    @PatchMapping("/{id}")
    public ResponseEntity<?> toggleGroup(@PathVariable int id) {
        taskGroupService.toggleGroup(id);
        return ResponseEntity.noContent().build();
    }


    @ModelAttribute("groups")
    List<GroupReadModel> getGroups() {
        return taskGroupService.readAll();
    }
}
