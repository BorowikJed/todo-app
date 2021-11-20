package eu.kijora.todoapp.controller;

import eu.kijora.todoapp.model.dto.ProjectWriteModel;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/projects")
public class ProjectController {

    @GetMapping
        //it's looking in templates for views to render
    String showProject(Model model) {
        ProjectWriteModel projectWriteModel = new ProjectWriteModel();
        projectWriteModel.setDescription("test input");
        model.addAttribute("project", projectWriteModel);
        return "projects";
    }
}
