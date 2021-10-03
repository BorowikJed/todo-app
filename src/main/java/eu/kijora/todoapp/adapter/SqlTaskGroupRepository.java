package eu.kijora.todoapp.adapter;

import eu.kijora.todoapp.model.TaskGroup;
import eu.kijora.todoapp.model.TaskGroupRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface SqlTaskGroupRepository extends JpaRepository<TaskGroup, Integer>, TaskGroupRepository {
}
