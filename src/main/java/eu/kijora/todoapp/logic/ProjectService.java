package eu.kijora.todoapp.logic;

import eu.kijora.todoapp.TaskConfigurationProperties;
import eu.kijora.todoapp.model.*;
import eu.kijora.todoapp.model.dto.GroupReadModel;
import eu.kijora.todoapp.model.dto.GroupTaskWriteModel;
import eu.kijora.todoapp.model.dto.GroupWriteModel;
import eu.kijora.todoapp.model.dto.ProjectWriteModel;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectService {
    private ProjectRepository projectRepository;
    private TaskGroupRepository taskGroupRepository;
    private TaskGroupService service;
    private TaskConfigurationProperties config;

    public ProjectService(ProjectRepository projectRepository, TaskGroupRepository taskGroupRepository, TaskGroupService service, TaskConfigurationProperties config) {
        this.projectRepository = projectRepository;
        this.taskGroupRepository = taskGroupRepository;
        this.service = service;
        this.config = config;
    }

    public List<Project> readAll() {
        return projectRepository.findAll();
    }

    public Project save(final ProjectWriteModel toSave) {
        return projectRepository.save(toSave.toProject());
    }

    public GroupReadModel createGroup(LocalDateTime deadline, int projectId) {
        if (!config.isAllowMultipleTasksFromTemplate() && taskGroupRepository.existsByDoneIsFalseAndProject_Id(projectId)) {
            throw new IllegalStateException("Only 1 undone group from project is allowed");
        }
        return projectRepository.findById(projectId)
                .map(project -> {
                    var targetGroup = new GroupWriteModel();
                    targetGroup.setDescription(project.getDescription());
                    targetGroup.setTasks(
                            project.getProjectSteps().stream()
                                    .map(projectStep -> {
                                        var task = new GroupTaskWriteModel();
                                        task.setDescription(projectStep.getDescription());
                                        task.setDeadline(deadline.plusDays(projectStep.getDaysToDeadline()));
                                        return task;
                                    }).collect(Collectors.toSet())
                    );
                    return service.createGroup(targetGroup, project);
                }).orElseThrow(()-> new IllegalArgumentException("Project with given id not found"));



//        TaskGroup taskGroup = projectRepository.findById(projectId)
//                .map(project -> {
//                    var result = new TaskGroup();
//                    result.setDescription(project.getDescription());
//                    result.setTasks(project.getProjectSteps().stream()
//                            .map(step ->
//                                    new Task(step.getDescription(), deadline.plusDays(step.getDaysToDeadline()))
//                            )
//                            .collect(Collectors.toSet())
//                    );
//                    result.setProject(project);
//                    return taskGroupRepository.save(result);
//                }).orElseThrow(() -> new IllegalArgumentException("Project with given ID not found"));
//        return new GroupReadModel(taskGroup);
    }

}
