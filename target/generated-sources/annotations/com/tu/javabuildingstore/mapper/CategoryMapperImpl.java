package com.tu.javabuildingstore.mapper;

import com.tu.javabuildingstore.dto.category.CategoryRequestDTO;
import com.tu.javabuildingstore.dto.category.CategoryResponseDTO;
import com.tu.javabuildingstore.model.Category;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-12-28T14:32:02+0200",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.9 (Microsoft)"
)
@Component
public class CategoryMapperImpl implements CategoryMapper {

    @Override
    public CategoryResponseDTO toDto(Category category) {
        if ( category == null ) {
            return null;
        }

        Long id = null;
        String name = null;
        String description = null;

        id = category.getId();
        name = category.getName();
        description = category.getDescription();

        CategoryResponseDTO categoryResponseDTO = new CategoryResponseDTO( id, name, description );

        return categoryResponseDTO;
    }

    @Override
    public Category toEntity(CategoryRequestDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Category.CategoryBuilder category = Category.builder();

        category.name( dto.name() );
        category.description( dto.description() );

        return category.build();
    }

    @Override
    public void updateFromDto(CategoryRequestDTO dto, Category category) {
        if ( dto == null ) {
            return;
        }

        if ( dto.name() != null ) {
            category.setName( dto.name() );
        }
        if ( dto.description() != null ) {
            category.setDescription( dto.description() );
        }
    }
}
