package com.tu.javabuildingstore.dto.user;

public record RegisterResponseDTO(
        Long id,
        String username,
        String email,
        String message,
        String token
) {
}
