package eu.kijora.todoapp.model;

import java.util.List;

public interface TaskRepository {

    List<Task> findAll();

    Task getById(Integer integer);

    <S extends Task> S save(S entity);

}

