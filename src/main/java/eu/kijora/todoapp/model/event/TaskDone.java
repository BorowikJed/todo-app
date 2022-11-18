package eu.kijora.todoapp.model.event;

import eu.kijora.todoapp.model.Task;

import java.time.Clock;

public class TaskDone extends TaskEvent{
    TaskDone(final Task source) {
        super(source.getId(), Clock.systemDefaultZone());
    }
}
