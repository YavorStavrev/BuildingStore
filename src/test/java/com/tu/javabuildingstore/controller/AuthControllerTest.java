package com.training.building_store.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.training.building_store.dto.user.AuthResponseDTO;
import com.training.building_store.dto.user.LoginRequestDTO;
import com.training.building_store.dto.user.RegisterRequestDTO;
import com.training.building_store.dto.user.RegisterResponseDTO;
import com.training.building_store.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    private ObjectMapper objectMapper = new ObjectMapper();

    private RegisterRequestDTO registerRequest;
    private RegisterResponseDTO registerResponse;
    private LoginRequestDTO loginRequest;
    private AuthResponseDTO authResponse;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();

        registerRequest = new RegisterRequestDTO("john", "john@test.com", "Password1", "John", "Doe");
        registerResponse = new RegisterResponseDTO(1L, "john", "john@test.com", "User registered successfully", "jwt-token");

        loginRequest = new LoginRequestDTO("john", "Password1");
        authResponse = new AuthResponseDTO("Login successful", "jwt-token");
    }

    @Test
    void testRegister() throws Exception {
        Mockito.when(authService.register(any(RegisterRequestDTO.class))).thenReturn(registerResponse);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("john")))
                .andExpect(jsonPath("$.token", is("jwt-token")));
    }

    @Test
    void testLogin() throws Exception {
        Mockito.when(authService.login(any(LoginRequestDTO.class))).thenReturn(authResponse);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Login successful")))
                .andExpect(jsonPath("$.token", is("jwt-token")));
    }
}
