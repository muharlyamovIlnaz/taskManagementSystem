package com.effective.mobile.tskmngmntsystm.controller.impl;

import com.effective.mobile.tskmngmntsystm.dto.CommentRq;
import com.effective.mobile.tskmngmntsystm.dto.CommentRs;
import com.effective.mobile.tskmngmntsystm.dto.TaskServiceResponse;
import com.effective.mobile.tskmngmntsystm.mapper.CommentMapper;
import com.effective.mobile.tskmngmntsystm.repository.CommentRepository;
import com.effective.mobile.tskmngmntsystm.repository.TaskRepository;
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

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
class CommentControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private CurrentUserUtil currentUserUtil;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }


    @Test
    @WithMockUser(username = "admin@test.com", roles = {"ADMIN"})
    void createCommentWhenCurrentUserIsAdmin() throws Exception {

        // given
        CommentRq commentRq = new CommentRq();

        commentRq.setText("Test text");
        commentRq.setTaskId(1L);


        // when
        String response = mockMvc.perform(post("/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentRq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Комментарий добавлен")))
                .andReturn()
                .getResponse()
                .getContentAsString();

        TaskServiceResponse<CommentRs> taskServiceResponse = objectMapper.readValue(response,
                new TypeReference<TaskServiceResponse<CommentRs>>() {
                });
        CommentRs body = taskServiceResponse.getBody();
        Long commentId = body.getId();

        // then
        assertTrue(commentRepository.existsById(commentId));

    }


    @Test
    @WithMockUser(username = "user2@test.com", roles = {"USER"})
    void createComment_WhenCurrentUserIsNotAdminAndNotPerformer() throws Exception {

        CommentRq commentRq = new CommentRq();

        commentRq.setText("Test text");
        commentRq.setTaskId(1L);


        mockMvc.perform(post("/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentRq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("вы не являетесь исполнителем данной задачи")));
    }

    @Test
    @WithMockUser(username = "user1@test.com", roles = {"USER"})
    void createComment_WhenCurrentUserIsPerformer() throws Exception {

        CommentRq commentRq = new CommentRq();

        commentRq.setText("Test text");
        commentRq.setTaskId(1L);


        mockMvc.perform(post("/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentRq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Комментарий добавлен")));
    }


    @Test
    @WithMockUser(username = "user2@test.com", roles = {"USER"})
    void deleteComment_WhenCurrentUserIsNotAuthor() throws Exception {
        // given
        Long id = 1L;

        // when - then
        mockMvc.perform(delete("/comment/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("вы можете удалить только свой комментарий")))
                .andExpect(jsonPath("$.status", is(400)));
    }

    @Test
    @WithMockUser(username = "user1@test.com", roles = {"USER"})
    void deleteComment_WhenCurrentUserIsAuthor() throws Exception {
        // given
        Long id = 1L;

        // when - then
        mockMvc.perform(delete("/comment/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Комментарий удален")))
                .andExpect(jsonPath("$.status", is(200)));

        assertFalse(commentRepository.existsById(id));
    }

}