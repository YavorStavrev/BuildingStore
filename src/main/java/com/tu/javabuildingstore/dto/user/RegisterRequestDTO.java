package com.tu.javabuildingstore.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequestDTO(

        @NotBlank
        String username,

        @NotBlank
        @Email
        String email,

        @Size(min = 8, max = 64, message = "Password must be between 8 and 64 characters")
        @Pattern(regexp = ".*\\d.*", message = "Password must contain at least one number")
        String password,

        @NotBlank(message = "User's first name cannot be null or empty")
        @Size(min = 2, max = 20, message = "User's first name must be between 2 and 20 characters")
        String firstName,

        @NotBlank(message = "User's last name cannot be null or empty")
        @Size(min = 2, max = 20, message = "User's last name must be between 2 and 20 characters")
        String lastName
) {
}
