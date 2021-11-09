package eu.kijora.todoapp.logic;

import eu.kijora.todoapp.model.Task;
import eu.kijora.todoapp.model.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

//for async connection to DB
//Needs @EnableAsync in 1 of @Configuration classes
@Service
public class TaskService {
    private final TaskRepository taskRepository;
    public static final Logger logger = LoggerFactory.getLogger(TaskService.class);

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    //action from future, pool
    @Async
    public CompletableFuture<List<Task>> findAllAsync(){
        logger.info("Execution from async");
        return CompletableFuture.supplyAsync(taskRepository::findAll);
    }
}
