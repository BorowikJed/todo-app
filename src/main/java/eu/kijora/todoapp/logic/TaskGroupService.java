package eu.kijora.todoapp.logic;

import eu.kijora.todoapp.TaskConfigurationProperties;
import eu.kijora.todoapp.model.TaskGroup;
import eu.kijora.todoapp.model.TaskGroupRepository;
import eu.kijora.todoapp.model.TaskRepository;
import eu.kijora.todoapp.model.dto.GroupReadModel;
import eu.kijora.todoapp.model.dto.GroupWriteModel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskGroupService {

    private TaskGroupRepository repository;
    private TaskRepository taskRepository;

    public TaskGroupService(TaskGroupRepository repository, TaskRepository taskRepository) {
        this.repository = repository;
        this.taskRepository = taskRepository;
    }

    public GroupReadModel createGroup(GroupWriteModel source) {
        TaskGroup saved = repository.save(source.toGroup());
        return new GroupReadModel(saved);
    }

    public List<GroupReadModel> readAll() {
        return repository.findAll().stream()
                .map(GroupReadModel::new)
                .collect(Collectors.toList());
    }

    public void toggleGroup(int groupId) {
        if (taskRepository.existsByDoneIsFalseAndGroup_Id(groupId)) {
            throw new IllegalStateException("Group has undone tasks!");
        } else {
            TaskGroup result = repository.findById(groupId).orElseThrow(() -> new IllegalArgumentException("Task group with given ID not found"));
            result.setDone(!result.isDone());
            repository.save(result);
        }

    }
}
