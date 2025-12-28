package com.tu.javabuildingstore.controller;

import com.tu.javabuildingstore.dto.user.AuthResponseDTO;
import com.tu.javabuildingstore.dto.user.LoginRequestDTO;
import com.tu.javabuildingstore.dto.user.RegisterRequestDTO;
import com.tu.javabuildingstore.dto.user.RegisterResponseDTO;
import com.tu.javabuildingstore.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDTO> register(@RequestBody RegisterRequestDTO dto) {
        return ResponseEntity.ok(authService.register(dto));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginRequestDTO dto) {
        return ResponseEntity.ok(authService.login(dto));
    }
}
