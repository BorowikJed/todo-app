package eu.kijora.todoapp;

import eu.kijora.todoapp.model.Task;
import eu.kijora.todoapp.model.TaskGroup;
import eu.kijora.todoapp.model.TaskRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.util.*;

@Configuration
public class TestConfiguration {

    @Bean
    @Primary
    @Profile("!integration")
    DataSource e2eTestDataSource() { //all the database runs, it's migrations...
        var result = new DriverManagerDataSource("jdbc:h2:mem:testdatabase;DB_CLOSE_DELAY=-1", "sa", "");
        result.setDriverClassName("org.h2.Driver");
        return result;
    }

    @Bean
    @Primary
    @Profile("integration")
    TaskRepository testRepo() {
        return new TaskRepository() {
            private Map<Integer, Task> tasks = new HashMap<>();

            @Override
            public List<Task> findAll() {
                return new ArrayList<>(tasks.values());
            }

            @Override
            public Optional<Task> findById(int id) {
                return Optional.ofNullable(tasks.get(id));
            }

            @Override
            public Task save(Task entity){
                int key = tasks.size() + 1;
                try {
                    var field = Task.class.getDeclaredField("id");
                    field.setAccessible(true);
                    field.set(entity, key);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
                tasks.put(key, entity);
                return entity;
            }

            @Override
            public boolean existsById(int id) {
                return false;
            }

            @Override
            public boolean existsByDoneIsFalseAndGroup_Id(Integer group_id) {
                return false;
            }

            @Override
            public List<Task> findByDone(Boolean done) {
                return null;
            }

            @Override
            public List<Task> findAllByGroup_Id(Integer groupId) {
                return null;
            }
        };

    }
}
