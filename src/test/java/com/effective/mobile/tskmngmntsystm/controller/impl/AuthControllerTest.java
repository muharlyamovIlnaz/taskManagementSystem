package com.effective.mobile.tskmngmntsystm.controller.impl;

import com.effective.mobile.tskmngmntsystm.dto.SignInRequest;
import com.effective.mobile.tskmngmntsystm.dto.SignUpRequest;
import com.effective.mobile.tskmngmntsystm.enums.Role;
import com.effective.mobile.tskmngmntsystm.models.UserEntity;
import com.effective.mobile.tskmngmntsystm.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
class AuthControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void signUp_shouldReturnOk_whenUserIsSuccessfullyCreated() throws Exception {
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setEmail("test@example.com");
        signUpRequest.setPassword("password123");
        signUpRequest.setRole("ROLE_USER");


        mockMvc.perform(post("/auth/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Клиент сохранен")));

        UserEntity savedUser = userRepository.findByEmail("test@example.com").orElseThrow();
        assertEquals("test@example.com", savedUser.getEmail());
        assertEquals(Role.ROLE_USER, savedUser.getRole());
    }

    @Test
    void signIn_shouldReturnToken_whenCredentialsAreValid() throws Exception {
        UserEntity existingUser = new UserEntity();
        existingUser.setEmail("test1@example.com");
        existingUser.setPassword(passwordEncoder.encode("password123"));
        existingUser.setRole(Role.ROLE_USER);
        userRepository.save(existingUser);

        SignInRequest signInRequest = new SignInRequest();
        signInRequest.setEmail("test1@example.com");
        signInRequest.setPassword("password123");

        mockMvc.perform(post("/auth/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signInRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Токен успешно получен")));

        UserEntity savedUser = userRepository.findByEmail("test1@example.com").orElseThrow();
        assertEquals("test1@example.com", savedUser.getEmail());
    }

    @Test
    void signIn_shouldReturnBadRequest_whenCredentialsAreInvalid() throws Exception {
        UserEntity existingUser = new UserEntity();
        existingUser.setEmail("test2@example.com");
        existingUser.setPassword(passwordEncoder.encode("password123"));
        existingUser.setRole(Role.ROLE_USER);
        userRepository.save(existingUser);

        SignInRequest signInRequest = new SignInRequest();
        signInRequest.setEmail("test2@example.com");
        signInRequest.setPassword("wrongpassword");


        mockMvc.perform(post("/auth/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signInRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Некорректный пароль")));
    }
}
