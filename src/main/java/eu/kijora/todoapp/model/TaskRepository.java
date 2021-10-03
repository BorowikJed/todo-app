package eu.kijora.todoapp.model;

import java.util.List;
import java.util.Optional;

public interface TaskRepository {

    List<Task> findAll();

    Optional<Task> findById(int id);

    Task save(Task entity);

    boolean existsById(int id);

    boolean existsByDoneIsFalseAndGroup_Id(Integer group_id);

}

