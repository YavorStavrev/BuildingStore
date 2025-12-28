package com.tu.javabuildingstore.dto.product;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record ProductRequestDTO(
        @NotBlank(message = "Product name is required")
        String name,

        @Size(max = 2000, message = "Description too long")
        String description,

        @NotNull(message = "Price is required")
        @DecimalMin(value = "0.0", inclusive = true, message = "Price must be >= 0")
        BigDecimal price,

        @DecimalMin(value = "0.0", inclusive = true, message = "Discount must be >= 0")
        @DecimalMax(value = "100.0", inclusive = true, message = "Discount must be <= 100")
        Double discountPercent,

        @NotNull(message = "Stock quantity is required")
        @Min(value = 0, message = "Stock must be >= 0")
        Integer stockQuantity,

        String imageUrl,

        Long categoryId


) {}
