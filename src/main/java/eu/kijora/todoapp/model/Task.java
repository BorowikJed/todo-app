package eu.kijora.todoapp.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotBlank(message = "Task's description must not be null!")
    private String description;
    private boolean done;
    private LocalDateTime deadline;
    @Embedded
//    @AttributeOverrides({ //when I want to just change name
//            @AttributeOverride(name="updatedOn", column = @Column(name="newNameWhichIWant"))
//    })
    private AuditDateTimes audit = new AuditDateTimes();
    @ManyToOne
    @JoinColumn(name = "task_group_id") //using this table (id) we are joining (getting 1 task we also get his group?)
    private TaskGroup group;

    public TaskGroup getGroup() {
        return group;
    }

    public Task() {
    }

    public Task(String description, LocalDateTime deadline) {
        this(description, deadline, null);
    }

    public Task(String description, LocalDateTime deadline, TaskGroup group) {
        this.description = description;
        this.deadline = deadline;
        if(group != null){
            this.group = group;
        }
    }

    void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    void setDescription(String description) {
        this.description = description;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public void updateFrom(final Task source){
        description = source.description;
        done = source.done;
        deadline = source.deadline;
        group = source.group;
    }

}
