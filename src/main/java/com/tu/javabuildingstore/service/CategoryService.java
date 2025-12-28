package com.tu.javabuildingstore.service;


import com.tu.javabuildingstore.dto.category.CategoryRequestDTO;
import com.tu.javabuildingstore.dto.category.CategoryResponseDTO;
import com.tu.javabuildingstore.exception.ResourceNotFoundException;
import com.tu.javabuildingstore.mapper.CategoryMapper;
import com.tu.javabuildingstore.model.Category;
import com.tu.javabuildingstore.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Transactional
    public CategoryResponseDTO saveCategory(CategoryRequestDTO dto) {
        Category category = categoryMapper.toEntity(dto);
        Category saved =  categoryRepository.save(category);
        return categoryMapper.toDto(saved);
    }

//    public Optional<Category> getCategoryById(Long id) {
//        return categoryRepository.findById(id);
//    }

    public List<CategoryResponseDTO> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(categoryMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Category.class, id));
        categoryRepository.delete(category);
    }

    @Transactional
    public CategoryResponseDTO pathCategoryName(Long id, String name) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Category.class, id));
        category.setName(name);
        return categoryMapper.toDto(category);
    }

    @Transactional
    public CategoryResponseDTO putCategory(Long id, CategoryRequestDTO dto) {
        Category fetched = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Category.class, id));
        categoryMapper.updateFromDto(dto, fetched);
        return categoryMapper.toDto(categoryRepository.save(fetched));
    }

    public CategoryResponseDTO getCategoryOrThrow(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Category.class, id));
        return categoryMapper.toDto(category);
    }

    @Transactional
    public Category getCategoryByNameOrCreate(String name) {
        Optional<Category> fetched = categoryRepository.findByName(name);
        return fetched.orElseGet(() -> categoryRepository.save(new Category(name)));
    }
}
