package com.tu.javabuildingstore.service;

import com.tu.javabuildingstore.dto.category.CategoryRequestDTO;
import com.tu.javabuildingstore.dto.category.CategoryResponseDTO;
import com.tu.javabuildingstore.exception.ResourceNotFoundException;
import com.tu.javabuildingstore.mapper.CategoryMapper;
import com.tu.javabuildingstore.model.Category;
import com.tu.javabuildingstore.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CategoryServiceTest {

    private CategoryRepository categoryRepository;
    private CategoryMapper categoryMapper;
    private CategoryService categoryService;

    private Category category;
    private CategoryResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        categoryRepository = mock(CategoryRepository.class);
        categoryMapper = mock(CategoryMapper.class);
        categoryService = new CategoryService(categoryRepository, categoryMapper);

        category = Category.builder()
                .id(1L)
                .name("Tools")
                .description("Construction tools")
                .products(new HashSet<>())
                .build();

        responseDTO = new CategoryResponseDTO(1L, "Tools", "Construction tools");
    }

    @Test
    void testSaveCategory_Success() {
        CategoryRequestDTO request = new CategoryRequestDTO("Tools", "Construction tools");

        when(categoryMapper.toEntity(request)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(responseDTO);

        CategoryResponseDTO result = categoryService.saveCategory(request);

        assertNotNull(result);
        assertEquals("Tools", result.name());
        verify(categoryRepository).save(category);
    }

    @Test
    void testGetAllCategories() {
        when(categoryRepository.findAll()).thenReturn(List.of(category));
        when(categoryMapper.toDto(category)).thenReturn(responseDTO);

        List<CategoryResponseDTO> all = categoryService.getAllCategories();

        assertEquals(1, all.size());
        assertEquals("Tools", all.get(0).name());
    }

    @Test
    void testGetCategoryOrThrow_NotFound() {
        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> categoryService.getCategoryOrThrow(99L));
    }

    @Test
    void testDeleteCategoryById_Success() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        doNothing().when(categoryRepository).delete(category);

        categoryService.deleteCategoryById(1L);

        verify(categoryRepository).delete(category);
    }

    @Test
    void testPatchCategoryName_Success() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryMapper.toDto(category)).thenReturn(responseDTO);

        CategoryResponseDTO result = categoryService.pathCategoryName(1L, "Updated Tools");

        assertNotNull(result);
        assertEquals("Tools", result.name()); // responseDTO е mock-нат
    }

    @Test
    void testPutCategory_Success() {
        CategoryRequestDTO request = new CategoryRequestDTO("Updated Tools", "Updated description");

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.save(category)).thenReturn(category);
        doNothing().when(categoryMapper).updateFromDto(request, category);
        when(categoryMapper.toDto(category)).thenReturn(responseDTO);

        CategoryResponseDTO result = categoryService.putCategory(1L, request);

        assertNotNull(result);
        verify(categoryRepository).save(category);
    }

    @Test
    void testGetCategoryByNameOrCreate_CreatesIfNotExist() {
        when(categoryRepository.findByName("New")).thenReturn(Optional.empty());
        when(categoryRepository.save(any(Category.class)))
                .thenReturn(Category.builder()
                        .id(2L)
                        .name("New")
                        .description(null)
                        .products(new HashSet<>())
                        .build());

        Category cat = categoryService.getCategoryByNameOrCreate("New");

        assertNotNull(cat);
        assertEquals("New", cat.getName());
    }
}

