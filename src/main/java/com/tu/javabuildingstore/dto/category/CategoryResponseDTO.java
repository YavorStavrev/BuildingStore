package com.tu.javabuildingstore.dto.category;

import com.tu.javabuildingstore.model.Category;

public record CategoryResponseDTO(
        Long id,
        String name,
        String description
) {
    public static CategoryResponseDTO fromEntity(Category category) {
        return new CategoryResponseDTO(
                category.getId(),
                category.getName(),
                category.getDescription());
    }
}
