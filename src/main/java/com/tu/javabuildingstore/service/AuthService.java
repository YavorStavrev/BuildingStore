package com.tu.javabuildingstore.service;

import com.tu.javabuildingstore.config.JwtProvider;
import com.tu.javabuildingstore.dto.user.AuthResponseDTO;
import com.tu.javabuildingstore.dto.user.LoginRequestDTO;
import com.tu.javabuildingstore.dto.user.RegisterRequestDTO;
import com.tu.javabuildingstore.dto.user.RegisterResponseDTO;
import com.tu.javabuildingstore.mapper.UserMapper;
import com.tu.javabuildingstore.model.User;
import com.tu.javabuildingstore.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public RegisterResponseDTO register(RegisterRequestDTO dto) {

        if (userRepository.existsByUsername(dto.username()))
            return userMapper.toResponse(null, "Username already in use");
        if (userRepository.existsByEmail(dto.email()))
            return userMapper.toResponse(null, "Email already in use");

        User user = userMapper.toEntity(dto);
        user.setPassword(passwordEncoder.encode(dto.password()));

        User saved = userRepository.save(user);

        String token = jwtProvider.generateToken(saved.getUsername());
        return new RegisterResponseDTO(saved.getId(), saved.getUsername(), saved.getEmail(), "User registered successfully", token);
    }

    public AuthResponseDTO login(LoginRequestDTO dto) {

        User user = userRepository.findByUsername(dto.username()).orElse(null);

        if (user == null)
            return new AuthResponseDTO("User not found", null);

        if (!passwordEncoder.matches(dto.password(), user.getPassword()))
            return new AuthResponseDTO("Invalid username or password", null);

        String token = jwtProvider.generateToken(user.getUsername());
        return new AuthResponseDTO("Login successful", token);
    }
}
