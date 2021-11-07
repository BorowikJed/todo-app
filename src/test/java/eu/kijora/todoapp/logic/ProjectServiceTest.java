package eu.kijora.todoapp.logic;

import eu.kijora.todoapp.TaskConfigurationProperties;
import eu.kijora.todoapp.model.*;
import eu.kijora.todoapp.model.dto.GroupReadModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProjectServiceTest {

    @Test
    @DisplayName("Should throw ISE when configured to allow just 1 group and undone group exists")
    void createGroup_noMultipleGroupsConfig_and_undoneGroups_throwsExceptionIllegalStateException() {
        //given
        var mockGroupRepository = groupRepositoryReturning(true);
        var mockConfig = getConfigReturning(false);

        var toTest = new ProjectService(null, mockGroupRepository, null, mockConfig);

        //when
        var exception = catchThrowable(() -> toTest.createGroup(LocalDateTime.now(), 1));

        //then
        assertThat(exception).
                isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("1 undone");

    }

    @Test
    @DisplayName("Should throw IAE when configured to allow just 1 group and undone group exists")
    void createGroup_configurationOk_And_noProjects_throws_IAE() {
        //given
        var mockConfig = getConfigReturning(true);
        var mockRepository = mock(ProjectRepository.class);
        when(mockRepository.findById(anyInt())).thenReturn(Optional.empty());
        var toTest = new ProjectService(mockRepository, null, null, mockConfig);

        //when
        var exception = catchThrowable(() -> toTest.createGroup(LocalDateTime.now(), 1));

        //then
        assertThat(exception).
                isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("given id not found");

    }

    @Test
    @DisplayName("Should throw IAE when configured to allow just 1 group and no groups and no projects for given id")
    void createGroup_noMultipleGroupsConfig_And_noUndoneGroups_And_noProjects_throws_IAE() {
        //given
        var mockConfig = getConfigReturning(true);
        var mockRepository = mock(ProjectRepository.class);
        when(mockRepository.findById(anyInt())).thenReturn(Optional.empty());
        var mockGroupRepository = groupRepositoryReturning(false);

        var toTest = new ProjectService(mockRepository, mockGroupRepository, null, mockConfig);

        //when
        var exception = catchThrowable(() -> toTest.createGroup(LocalDateTime.now(), 1));

        //then
        assertThat(exception).
                isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("given id not found");

    }

    @Test
    @DisplayName("Should create a new group from project")
    void createGroup_configOk_existingProject_createsAndSavesGroup() {
        //given
        var today = LocalDate.now().atStartOfDay();
        var mockRepository = mock(ProjectRepository.class);
        var project = projectWith("bar", Set.of(-1,-2,-3));
        when(mockRepository.findById(anyInt()))
                .thenReturn(Optional.of(project));
        var mockConfig = getConfigReturning(true);
        InMemoryGroupRepository inMemoryGroupRepo = inMemoryGroupRepository();
        var serviceWithInMemRepo = new TaskGroupService(inMemoryGroupRepo, null);
        int countBeforeCall = inMemoryGroupRepo.count();
        var toTest = new ProjectService(mockRepository, inMemoryGroupRepo, serviceWithInMemRepo, mockConfig);

        //when
        GroupReadModel result = toTest.createGroup(today, 1);

        //then
        assertThat(result.getDescription()).isEqualTo("bar");
        assertThat(result.getDeadline()).isEqualTo(today.minusDays(1));
        assertThat(result.getTasks()).allMatch(task -> task.getDescription().equals("foo"));
        assertThat(countBeforeCall + 1).isEqualTo(inMemoryGroupRepo.count());
    }

    private Project projectWith(String projectDescription, Set<Integer> daysToDeadline) {

        Set<ProjectStep> steps = daysToDeadline.stream()
                .map(dayToDeadline -> {
                    var step = mock(ProjectStep.class);
                    when(step.getDescription()).thenReturn("foo");
                    when(step.getDaysToDeadline()).thenReturn(dayToDeadline);
                    return step;
                }).collect(Collectors.toSet());
        var result = mock(Project.class);
        when(result.getDescription()).thenReturn(projectDescription);
        when(result.getProjectSteps()).thenReturn(steps);
        return result;
    }

    private InMemoryGroupRepository inMemoryGroupRepository() {
        return new InMemoryGroupRepository();
    }

    private static class InMemoryGroupRepository implements TaskGroupRepository {
        private Map<Integer, TaskGroup> map = new HashMap<>();
        private int index = 0;

        public int count() {
            return map.size();
        }

        @Override
        public List<TaskGroup> findAll() {
            return new ArrayList<>(map.values());
        }

        @Override
        public Optional<TaskGroup> findById(Integer id) {
            return Optional.ofNullable(map.get(id));
        }

        @Override
        public TaskGroup save(final TaskGroup entity) {
            if (entity.getId() == 0) {
                try {
                    Field field = TaskGroup.class.getDeclaredField("id");
                    field.setAccessible(true);
                    field.set(entity, ++index);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    throw new RuntimeException();
                }
            }
            map.put(entity.getId(), entity);
            return entity;
        }

        @Override
        public boolean existsByDoneIsFalseAndProject_Id(Integer projectId) {
            return map.values().stream()
                    .filter(group -> !group.isDone())
                    .anyMatch(group -> group.getProject() != null && group.getProject().getId() == projectId);
        }
    }


    private TaskGroupRepository groupRepositoryReturning(final boolean result) {
        var mockGroupRepository = mock(TaskGroupRepository.class);
        when(mockGroupRepository.existsByDoneIsFalseAndProject_Id(anyInt())).thenReturn(result);
        return mockGroupRepository;
    }


    private TaskConfigurationProperties getConfigReturning(final boolean result) {
        var mockConfig = mock(TaskConfigurationProperties.class);
        when(mockConfig.isAllowMultipleTasksFromTemplate()).thenReturn(result);
        return mockConfig;
    }

    //        try {
//            toTest.createGroup(LocalDateTime.now(), 1);
//        }
//        //then
//        catch (Exception e) {
//            assertThat(e).isInstanceOf(IllegalStateException.class);
//        }

}