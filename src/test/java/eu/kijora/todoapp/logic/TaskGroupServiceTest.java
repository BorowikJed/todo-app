package eu.kijora.todoapp.logic;

import eu.kijora.todoapp.model.TaskGroup;
import eu.kijora.todoapp.model.TaskGroupRepository;
import eu.kijora.todoapp.model.TaskRepository;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TaskGroupServiceTest {

    @Test
    void toggleGroup_throwsISE_whenUndoneTasks() {
        //given
        TaskRepository mockTaskRepository = getTaskRepositoryWith(true);
        var toTest = new TaskGroupService(null, mockTaskRepository);

        //when
        var ex = catchThrowable(() -> toTest.toggleGroup(1));

        //then
        assertThat(ex).isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("undone tasks");
    }

    @Test
    void toggleGroup_throwsIArgE_whenWrongId() {

        //given
        TaskRepository mockTaskRepository = getTaskRepositoryWith(false);
        var mockTaskGroupRepository = mock(TaskGroupRepository.class);
        when(mockTaskGroupRepository.findById(anyInt())).thenReturn(Optional.empty());
        var toTest = new TaskGroupService(mockTaskGroupRepository, mockTaskRepository);

        //when
        var ex = catchThrowable(() -> toTest.toggleGroup(1));

        //then
        assertThat(ex).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(" with given ID not found");

    }

    @Test
    void toggleGroup_happyPath() {

        //given
        TaskRepository mockTaskRepository = getTaskRepositoryWith(false);
        var group = new TaskGroup();
        var beforeToggle = group.isDone();
        var mockTaskGroupRepository = mock(TaskGroupRepository.class);
        when(mockTaskGroupRepository.findById(anyInt())).thenReturn(Optional.of(group));
        var toTest = new TaskGroupService(mockTaskGroupRepository, mockTaskRepository);

        //when
        toTest.toggleGroup(1);

        //then
        assertThat(group.isDone()).isEqualTo(!beforeToggle);

    }

    private TaskRepository getTaskRepositoryWith(final boolean result) {
        var mockTaskRepository = mock(TaskRepository.class);
        when(mockTaskRepository.existsByDoneIsFalseAndGroup_Id(anyInt())).thenReturn(result);
        return mockTaskRepository;
    }
}