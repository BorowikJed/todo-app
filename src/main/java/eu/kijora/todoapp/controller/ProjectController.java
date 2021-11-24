package eu.kijora.todoapp.controller;

import eu.kijora.todoapp.logic.ProjectService;
import eu.kijora.todoapp.model.Project;
import eu.kijora.todoapp.model.ProjectStep;
import eu.kijora.todoapp.model.dto.ProjectWriteModel;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/projects")
public class ProjectController {

    private ProjectService service;

    public ProjectController(ProjectService service) {
        this.service = service;
    }

    @GetMapping
        //it's looking in templates for views to render
    String showProject(Model model) {
        ProjectWriteModel projectWriteModel = new ProjectWriteModel();
        projectWriteModel.setDescription("test input");
        model.addAttribute("project", projectWriteModel);
        return "projects";
    }

    @PostMapping(params = "addStep")
    String addProjectStep(@ModelAttribute("project") ProjectWriteModel current){ //How does it know it's THE projectWriteModel?
        current.getSteps().add(new ProjectStep());
        return "projects";
    }

    @PostMapping
    String addProject(@ModelAttribute("project") ProjectWriteModel current, Model model){
        service.save(current);
        model.addAttribute("project", new ProjectWriteModel());
        model.addAttribute("message", "Project added");
        return "projects";

    }

    @ModelAttribute("projects") //how does it actually work? Injecting model to each view?
    List<Project> getProjects(){
        return service.readAll();
    }
}
