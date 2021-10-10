package eu.kijora.todoapp;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties("task")
@Configuration //Should not be needed from Spring boot 2.X something. Strange.
public class TaskConfigurationProperties {
    private boolean allowMultipleTasksFromTemplate;

    public boolean isAllowMultipleTasksFromTemplate() {
        return allowMultipleTasksFromTemplate;
    }

}
