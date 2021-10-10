package eu.kijora.todoapp.model.special;

import eu.kijora.todoapp.model.Task;

public class GroupTaskReadModel { //let's say to read task in group we just want to show description and done (DTO)
    private String description;
    private boolean done;

    public GroupTaskReadModel(Task source) {
        description = source.getDescription();
        done = source.isDone();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
