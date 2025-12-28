package com.tu.javabuildingstore.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record LoginRequestDTO(

        @NotBlank
        String username,

        @NotBlank
        @Size(min = 8, max = 64, message = "Password must be between 8 and 64 characters")
        @Pattern(regexp = ".*\\d.*", message = "Password must contain at least one number")
        String password
) {
}
