package eu.kijora.todoapp.controller;

import eu.kijora.todoapp.model.Task;
import eu.kijora.todoapp.model.TaskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;


@WebMvcTest(TaskController.class)
public class TaskControllerLightIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private TaskRepository repository;

    @Test
    void httpGet_returnsGivenTask() throws Exception {
        //given
        Task foo = new Task("foo", LocalDateTime.now());
        when(repository.findById(anyInt())).thenReturn(Optional.of(foo));


        //when + then
        mockMvc.perform(MockMvcRequestBuilders.get("/tasks/123"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());

    }
}
