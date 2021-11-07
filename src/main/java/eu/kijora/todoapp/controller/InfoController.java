package eu.kijora.todoapp.controller;

import eu.kijora.todoapp.TaskConfigurationProperties;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/info")
public class InfoController {
    private DataSourceProperties dataSourceProperties;
    private TaskConfigurationProperties properties;

    public InfoController(DataSourceProperties dataSourceProperties, TaskConfigurationProperties properties) {
        this.dataSourceProperties = dataSourceProperties;
        this.properties = properties;
    }

    @GetMapping("/url")
    String url() {
        return dataSourceProperties.getUrl();
    }

    @GetMapping("/prop")
    boolean myProp(){
        return properties.isAllowMultipleTasksFromTemplate();
    }
}
