package eu.kijora.todoapp.adapter;

import eu.kijora.todoapp.model.Project;
import eu.kijora.todoapp.model.ProjectRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

interface SqlProjectRepository extends JpaRepository<Project, Integer>, ProjectRepository {

    @Override
    @Query("from Project p join fetch p.projectSteps")
    List<Project> findAll();
}
