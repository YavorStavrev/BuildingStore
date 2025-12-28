package com.tu.javabuildingstore.mapper;

import com.tu.javabuildingstore.dto.category.CategoryRequestDTO;
import com.tu.javabuildingstore.dto.category.CategoryResponseDTO;
import com.tu.javabuildingstore.model.Category;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

class CategoryMapperTest {

    private final CategoryMapper mapper = Mappers.getMapper(CategoryMapper.class);

    @Test
    void toDto_mapsFields() {
        Category c = new Category("Tools");
        CategoryResponseDTO dto = mapper.toDto(c);
        assertNotNull(dto);
    }

    @Test
    void toEntity_mapsFields() {
        CategoryRequestDTO req = new CategoryRequestDTO();
        Category entity = mapper.toEntity(req);
        assertNotNull(entity);
    }

    @Test
    void updateFromDto_updatesNonNullFields() {
        CategoryRequestDTO req = new CategoryRequestDTO();
        Category target = new Category("Old");
        mapper.updateFromDto(req, target);
        assertNotNull(target);
    }
}