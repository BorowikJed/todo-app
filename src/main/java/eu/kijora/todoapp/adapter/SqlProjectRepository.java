package eu.kijora.todoapp.adapter;

import eu.kijora.todoapp.model.Project;
import eu.kijora.todoapp.model.ProjectRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

interface SqlProjectRepository extends JpaRepository<Project, Integer>, ProjectRepository {

    @Override
    @Query("select distinct p from Project p join fetch p.projectSteps") //default join is inner join
    List<Project> findAll();
}
