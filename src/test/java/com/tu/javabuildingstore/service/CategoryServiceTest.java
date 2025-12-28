package com.tu.javabuildingstore.service;

import com.tu.javabuildingstore.dto.category.CategoryRequestDTO;
import com.tu.javabuildingstore.dto.category.CategoryResponseDTO;
import com.tu.javabuildingstore.exception.ResourceNotFoundException;
import com.tu.javabuildingstore.mapper.CategoryMapper;
import com.tu.javabuildingstore.model.Category;
import com.tu.javabuildingstore.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategoryServiceTest {

    private CategoryRepository repository;
    private CategoryMapper mapper;
    private CategoryService service;

    @BeforeEach
    void setUp() {
        repository = mock(CategoryRepository.class);
        mapper = mock(CategoryMapper.class);
        service = new CategoryService(repository, mapper);
    }

    @Test
    void saveCategory_mapsAndPersists() {
        CategoryRequestDTO req = new CategoryRequestDTO("Pesho", "e debel");
        Category entity = new Category("Tools");
        Category saved = new Category("Tools");
        CategoryResponseDTO resp = new CategoryResponseDTO();

        when(mapper.toEntity(req)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(saved);
        when(mapper.toDto(saved)).thenReturn(resp);

        CategoryResponseDTO out = service.saveCategory(req);

        assertNotNull(out);
        verify(mapper).toEntity(req);
        verify(repository).save(entity);
        verify(mapper).toDto(saved);
    }

    @Test
    void getAllCategories_mapsList() {
        Category c1 = new Category("A");
        Category c2 = new Category("B");
        when(repository.findAll()).thenReturn(List.of(c1, c2));
        when(mapper.toDto(c1)).thenReturn(new CategoryResponseDTO());
        when(mapper.toDto(c2)).thenReturn(new CategoryResponseDTO());

        List<CategoryResponseDTO> out = service.getAllCategories();

        assertEquals(2, out.size());
        verify(repository).findAll();
        verify(mapper).toDto(c1);
        verify(mapper).toDto(c2);
    }

    @Test
    void deleteCategoryById_exists() {
        Category c = new Category("X");
        when(repository.findById(1L)).thenReturn(Optional.of(c));

        service.deleteCategoryById(1L);

        verify(repository).delete(c);
    }

    @Test
    void deleteCategoryById_missing_throws() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.deleteCategoryById(99L));
        verify(repository, never()).delete(any());
    }

    @Test
    void pathCategoryName_updatesAndReturnsDto() {
        Category c = new Category("Old");
        when(repository.findById(5L)).thenReturn(Optional.of(c));
        CategoryResponseDTO resp = new CategoryResponseDTO();
        when(mapper.toDto(c)).thenReturn(resp);

        CategoryResponseDTO out = service.pathCategoryName(5L, "New");

        assertNotNull(out);
        assertEquals("New", c.getName());
        verify(mapper).toDto(c);
    }

    @Test
    void putCategory_updatesWithMapper() {
        Category existing = new Category("Old");
        when(repository.findById(7L)).thenReturn(Optional.of(existing));
        CategoryRequestDTO req = new CategoryRequestDTO();

        doAnswer(inv -> {
            existing.setName("Updated");
            return null;
        }).when(mapper).updateFromDto(req, existing);

        when(repository.save(existing)).thenReturn(existing);
        when(mapper.toDto(existing)).thenReturn(new CategoryResponseDTO());

        CategoryResponseDTO out = service.putCategory(7L, req);

        assertNotNull(out);
        verify(mapper).updateFromDto(req, existing);
        verify(repository).save(existing);
        verify(mapper).toDto(existing);
    }

    @Test
    void getCategoryOrThrow_found() {
        Category c = new Category("Z");
        when(repository.findById(2L)).thenReturn(Optional.of(c));
        when(mapper.toDto(c)).thenReturn(new CategoryResponseDTO());

        CategoryResponseDTO out = service.getCategoryOrThrow(2L);

        assertNotNull(out);
        verify(mapper).toDto(c);
    }

    @Test
    void getCategoryOrThrow_missing_throws() {
        when(repository.findById(3L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.getCategoryOrThrow(3L));
    }

    @Test
    void getCategoryByNameOrCreate_existing() {
        Category existing = new Category("Tools");
        when(repository.findByName("Tools")).thenReturn(Optional.of(existing));

        Category out = service.getCategoryByNameOrCreate("Tools");

        assertSame(existing, out);
        verify(repository, never()).save(any());
    }

    @Test
    void getCategoryByNameOrCreate_creates() {
        when(repository.findByName("New")).thenReturn(Optional.empty());
        when(repository.save(Mockito.any(Category.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        Category out = service.getCategoryByNameOrCreate("New");

        assertEquals("New", out.getName());
        verify(repository).save(any(Category.class));
    }
}
