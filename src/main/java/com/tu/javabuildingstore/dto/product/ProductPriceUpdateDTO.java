package com.tu.javabuildingstore.dto.product;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ProductPriceUpdateDTO(
        @NotNull
        @DecimalMin(value = "0.0", inclusive = true)
        BigDecimal price
) {
}