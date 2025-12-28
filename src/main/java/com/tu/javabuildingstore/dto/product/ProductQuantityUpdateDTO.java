package com.tu.javabuildingstore.dto.product;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ProductQuantityUpdateDTO(
        @NotNull
        @Min(value = 0, message = "Stock must be >= 0")
        Integer stockQuantity
) {
}
