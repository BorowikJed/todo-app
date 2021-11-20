package eu.kijora.todoapp.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@Entity
@Table(name = "projects")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotBlank(message = "Task's description must not be null!")
    private String description;
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    //because when e.g. deleting Project we want to delete all it's steps
    private Set<ProjectStep> projectSteps;
    @OneToMany(mappedBy = "project") //with mappedBy it's bidirectional. It could be like a Product is not aware
    // it's in a Cart (uni) but if product has some Cart Id then it's bidrectional
    private Set<TaskGroup> taskGroups;

    public Project() {
    }

    public int getId() {
        return id;
    }

    void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<ProjectStep> getProjectSteps() {
        return projectSteps;
    }

    public void setProjectSteps(Set<ProjectStep> projectSteps) {
        this.projectSteps = projectSteps;
    }

    public Set<TaskGroup> getTaskGroups() {
        return taskGroups;
    }

    public void setTaskGroups(Set<TaskGroup> taskGroups) {
        this.taskGroups = taskGroups;
    }
}
