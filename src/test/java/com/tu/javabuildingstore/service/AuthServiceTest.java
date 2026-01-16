package com.tu.javabuildingstore.service;

import com.tu.javabuildingstore.config.JwtProvider;
import com.tu.javabuildingstore.dto.user.AuthResponseDTO;
import com.tu.javabuildingstore.dto.user.LoginRequestDTO;
import com.tu.javabuildingstore.dto.user.RegisterRequestDTO;
import com.tu.javabuildingstore.dto.user.RegisterResponseDTO;
import com.tu.javabuildingstore.mapper.UserMapper;
import com.tu.javabuildingstore.model.User;
import com.tu.javabuildingstore.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AuthServiceTest {

    private UserRepository userRepository;
    private UserMapper userMapper;
    private PasswordEncoder passwordEncoder;
    private JwtProvider jwtProvider;
    private AuthService authService;

    private User sampleUser;
    private RegisterRequestDTO registerRequest;
    private LoginRequestDTO loginRequest;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userMapper = mock(UserMapper.class);
        passwordEncoder = mock(PasswordEncoder.class);
        jwtProvider = mock(JwtProvider.class);

        authService = new AuthService(userRepository, userMapper, passwordEncoder, jwtProvider);

        sampleUser = new User();
        sampleUser.setId(1L);
        sampleUser.setUsername("john");
        sampleUser.setEmail("john@test.com");
        sampleUser.setPassword("encodedPassword");

        registerRequest = new RegisterRequestDTO(
                "john", "john@test.com", "Password1", "John", "Doe"
        );

        loginRequest = new LoginRequestDTO("john", "Password1");

        // Ensure mocked mapper returns non-null responses for conflict branches
        when(userMapper.toResponse(null, "Username already in use"))
                .thenReturn(new RegisterResponseDTO(null, null, null, "Username already in use", null));
        when(userMapper.toResponse(null, "Email already in use"))
                .thenReturn(new RegisterResponseDTO(null, null, null, "Email already in use", null));
    }

    @Test
    void testRegister_Success() {
        when(userRepository.existsByUsername("john")).thenReturn(false);
        when(userRepository.existsByEmail("john@test.com")).thenReturn(false);
        when(userMapper.toEntity(registerRequest)).thenReturn(sampleUser);
        when(passwordEncoder.encode("Password1")).thenReturn("encodedPassword");
        when(userRepository.save(sampleUser)).thenReturn(sampleUser);
        when(jwtProvider.generateToken("john")).thenReturn("jwt-token");

        RegisterResponseDTO response = authService.register(registerRequest);

        assertNotNull(response);
        assertEquals("john", response.username());
        assertEquals("jwt-token", response.token());
        assertEquals("User registered successfully", response.message());
    }

    @Test
    void testRegister_UsernameExists() {
        when(userRepository.existsByUsername("john")).thenReturn(true);

        RegisterResponseDTO response = authService.register(registerRequest);

        assertNotNull(response);
        assertEquals("Username already in use", response.message());
        assertNull(response.token());
    }

    @Test
    void testRegister_EmailExists() {
        when(userRepository.existsByUsername("john")).thenReturn(false);
        when(userRepository.existsByEmail("john@test.com")).thenReturn(true);

        RegisterResponseDTO response = authService.register(registerRequest);

        assertNotNull(response);
        assertEquals("Email already in use", response.message());
        assertNull(response.token());
    }

    @Test
    void testLogin_Success() {
        when(userRepository.findByUsername("john")).thenReturn(Optional.of(sampleUser));
        when(passwordEncoder.matches("Password1", "encodedPassword")).thenReturn(true);
        when(jwtProvider.generateToken("john")).thenReturn("jwt-token");

        AuthResponseDTO response = authService.login(loginRequest);

        assertEquals("Login successful", response.message());
        assertEquals("jwt-token", response.token());
    }

    @Test
    void testLogin_UserNotFound() {
        when(userRepository.findByUsername("john")).thenReturn(Optional.empty());

        AuthResponseDTO response = authService.login(loginRequest);

        assertEquals("User not found", response.message());
        assertNull(response.token());
    }

    @Test
    void testLogin_InvalidPassword() {
        when(userRepository.findByUsername("john")).thenReturn(Optional.of(sampleUser));
        when(passwordEncoder.matches("Password1", "encodedPassword")).thenReturn(false);

        AuthResponseDTO response = authService.login(loginRequest);

        assertEquals("Invalid username or password", response.message());
        assertNull(response.token());
    }
}
