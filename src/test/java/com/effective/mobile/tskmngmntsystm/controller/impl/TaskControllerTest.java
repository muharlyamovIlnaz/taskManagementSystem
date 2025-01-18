package com.effective.mobile.tskmngmntsystm.controller.impl;

import com.effective.mobile.tskmngmntsystm.dto.TaskRq;
import com.effective.mobile.tskmngmntsystm.dto.TaskRs;
import com.effective.mobile.tskmngmntsystm.dto.TaskServiceResponse;
import com.effective.mobile.tskmngmntsystm.enums.Priority;
import com.effective.mobile.tskmngmntsystm.enums.Status;
import com.effective.mobile.tskmngmntsystm.mapper.TaskMapper;
import com.effective.mobile.tskmngmntsystm.models.TaskEntity;
import com.effective.mobile.tskmngmntsystm.repository.TaskRepository;
import com.effective.mobile.tskmngmntsystm.repository.UserRepository;
import com.effective.mobile.tskmngmntsystm.util.CurrentUserUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
class TaskControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private CurrentUserUtil currentUserUtil;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @WithMockUser(username = "admin@test.com", roles = {"ADMIN"})
    void createTask_WhenValidRequest_ReturnsCreatedTask() throws Exception {

        // given

        TaskRq taskRq = new TaskRq();
        taskRq.setTitle("Новая задача");
        taskRq.setDescription("Описание новой задачи");
        taskRq.setStatus(Status.PENDING);
        taskRq.setPriority(Priority.MEDIUM);
        taskRq.setPerformerId(2L);

        // when - then
        mockMvc.perform(post("/task")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskRq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Задача создана")));
    }

    @Test
    @WithMockUser(username = "admin@test.com", roles = {"ADMIN"})
    void createTask_WhenInvalidRequest_ReturnsError() throws Exception {

        // given
        TaskRq taskRq = new TaskRq();

        // when - then
        mockMvc.perform(post("/task")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskRq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Не удалось создать задачу, возможно такая задача уже существует")));
    }


    @Test
    @WithMockUser(username = "admin@test.com", roles = {"ADMIN"})
    void updateTask_WhenValidRequest_ReturnsUpdatedTask() throws Exception {

        // given
        TaskRq taskRq = new TaskRq();
        taskRq.setTitle("Обновленная задача");
        taskRq.setDescription("Обновленное описание");
        taskRq.setStatus(Status.IN_PROGRESS);
        taskRq.setPriority(Priority.HIGH);
        taskRq.setPerformerId(2L);


        // when - then
        mockMvc.perform(put("/task/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskRq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Задача сохранена")));
    }


    @Test
    @WithMockUser(username = "user1@test.com", roles = {"ADMIN"})
    void updateTask_WhenTaskNotFound_ReturnsError() throws Exception {

        // given
        TaskRq taskRq = new TaskRq();
        taskRq.setTitle("Не существующая задача");
        taskRq.setDescription("Описание не существующей задачи");
        taskRq.setStatus(Status.PENDING);
        taskRq.setPriority(Priority.LOW);
        taskRq.setPerformerId(2L);

        // when - then
        mockMvc.perform(put("/task/{id}", 999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskRq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Ошибка при обновлении задачи ")));
    }


    @Test
    @WithMockUser(username = "user1@test.com", roles = {"USER"})
    void updateTaskStatus_WhenValidRequest_ReturnsUpdatedStatus() throws Exception {
        // given - when - then
        mockMvc.perform(put("/task/{id}/status", 1L)
                        .param("newStatus", "COMPLETED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Статус изменен")));
    }

    @Test
    @WithMockUser(username = "user2@test.com", roles = {"USER"})
    void updateTaskStatus_WhenUserIsNotPerformer_ReturnsError() throws Exception {
        // given - when - then
        mockMvc.perform(put("/task/{id}/status", 1L)
                        .param("newStatus", "IN_PROGRESS"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Вы не являетесь исполнителем данной задачи")));
    }


    @Test
    @WithMockUser(username = "admin@test.com", roles = {"ADMIN"})
    void deleteTask_WhenTaskExists_ReturnsSuccess() throws Exception {

        // given
        TaskRq taskRq = new TaskRq();
        taskRq.setTitle("Еще одна новая задача");
        taskRq.setDescription("Описание");
        taskRq.setStatus(Status.PENDING);
        taskRq.setPriority(Priority.LOW);
        taskRq.setPerformerId(2L);

        TaskEntity taskEntity = taskMapper.toTaskEntity(taskRq);
        taskEntity.setAuthorId(1L);
        taskRepository.save(taskEntity);

        // when - then
        mockMvc.perform(delete("/task/{id}", 5L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Задача, с id = 5, удалена")));
    }

    @Test
    @WithMockUser(username = "admin@test.com", roles = {"ADMIN"})
    void deleteTask_WhenTaskNotFound_ReturnsError() throws Exception {
        // given - when - then
        mockMvc.perform(delete("/task/{id}", 999L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Задача, с id = " + 999L + ", удалена")));
    }

    @Test
    @WithMockUser(username = "user1@test.com", roles = {"USER"})
    void getTaskById_WhenTaskExists_ReturnsTask() throws Exception {
        // given - when - then
        mockMvc.perform(get("/task/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Задача с id = 1 найдена")));
    }

    @Test
    @WithMockUser(username = "user1@test.com", roles = {"USER"})
    void getTaskById_WhenTaskNotFound_ReturnsError() throws Exception {
        // given - when - then
        mockMvc.perform(get("/task/{id}", 999L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Задача с id 999 не найдена")));
    }

    @Test
    @WithMockUser(username = "user1@test.com", roles = {"USER"})
    void getAllTasks_WhenValidRequest_ReturnsTaskList() throws Exception {
        // given - when - then
        mockMvc.perform(get("/task")
                        .param("authorId", "1")
                        .param("performerId", "2")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Успешно получен список всех задач")));
    }

    @Test
    @WithMockUser(username = "user1@test.com", roles = {"USER"})
    void getAllTasks_WhenInvalidRequest_ReturnsError() throws Exception {
        // given - when - then
        String response = mockMvc.perform(get("/task")
                        .param("authorId", "7")
                        .param("performerId", "7")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Успешно получен список всех задач")))
                .andReturn()
                .getResponse()
                .getContentAsString();

        TaskServiceResponse<List<TaskRs>> taskServiceResponse = objectMapper.readValue(
                response,
                new TypeReference<TaskServiceResponse<List<TaskRs>>>() {
                }
        );
        assertTrue(taskServiceResponse.getBody().isEmpty());
    }


}