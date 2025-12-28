package com.tu.javabuildingstore.mapper;


import com.tu.javabuildingstore.dto.category.CategoryRequestDTO;
import com.tu.javabuildingstore.dto.category.CategoryResponseDTO;
import com.tu.javabuildingstore.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CategoryMapper {
    CategoryResponseDTO toDto(Category category);

    Category toEntity(CategoryRequestDTO dto);

    void updateFromDto(CategoryRequestDTO dto, @MappingTarget Category category);
}
