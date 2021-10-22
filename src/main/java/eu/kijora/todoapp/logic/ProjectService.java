package eu.kijora.todoapp.logic;

import eu.kijora.todoapp.TaskConfigurationProperties;
import eu.kijora.todoapp.model.*;
import eu.kijora.todoapp.model.dto.GroupReadModel;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class ProjectService {
    private ProjectRepository projectRepository;
    private TaskGroupRepository taskGroupRepository;
    private TaskConfigurationProperties config;

    public ProjectService(ProjectRepository projectRepository, TaskGroupRepository taskGroupRepository, TaskConfigurationProperties config) {
        this.projectRepository = projectRepository;
        this.taskGroupRepository = taskGroupRepository;
        this.config = config;
    }

    public List<Project> readAll() {
        return projectRepository.findAll();
    }

    public Project save(final Project toSave) {
        return projectRepository.save(toSave);
    }

    public GroupReadModel createGroup(LocalDateTime deadline, int projectId) {
        if (!config.isAllowMultipleTasksFromTemplate() && taskGroupRepository.existsByDoneIsFalseAndProject_Id(projectId)) {
            throw new IllegalStateException("Only 1 undone group from project is allowed");
        }
        TaskGroup taskGroup = projectRepository.findById(projectId)
                .map(project -> {
                    var result = new TaskGroup();
                    result.setDescription(project.getDescription());
                    result.setTasks(project.getProjectSteps().stream()
                            .map(step ->
                                    new Task(step.getDescription(), deadline.plusDays(step.getDaysToDeadline()))
                            )
                            .collect(Collectors.toSet())
                    );
                    return result;
                }).orElseThrow(() -> new IllegalArgumentException("Project with given ID not found"));
        return new GroupReadModel(taskGroup);
    }

}
