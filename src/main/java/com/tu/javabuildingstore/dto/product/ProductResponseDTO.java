package com.tu.javabuildingstore.dto.product;

import com.tu.javabuildingstore.dto.category.CategoryResponseDTO;

import java.math.BigDecimal;

public record ProductResponseDTO(
        Long id,
        String name,
        String description,
        BigDecimal price,
        Double discountPercent,
        BigDecimal discountedPrice,
        Integer stockQuantity,
        String imageUrl,
        CategoryResponseDTO category
) {}

