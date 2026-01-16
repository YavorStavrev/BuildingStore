package com.tu.javabuildingstore.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.tu.javabuildingstore.dto.category.CategoryRequestDTO;
import com.tu.javabuildingstore.dto.category.CategoryResponseDTO;
import com.tu.javabuildingstore.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class CategoryControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CategoryController categoryController;

    private ObjectMapper objectMapper = new ObjectMapper();

    private CategoryResponseDTO sampleCategory;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(categoryController).build();

        sampleCategory = new CategoryResponseDTO(1L, "Tools", "Construction tools");
    }

    @Test
    void testGetAll() throws Exception {
        Mockito.when(categoryService.getAllCategories()).thenReturn(List.of(sampleCategory));

        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is("Tools")));
    }

    @Test
    void testGetById() throws Exception {
        Mockito.when(categoryService.getCategoryOrThrow(1L)).thenReturn(sampleCategory);

        mockMvc.perform(get("/api/categories/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Tools")));
    }

    @Test
    void testCreateCategory() throws Exception {
        CategoryRequestDTO request = new CategoryRequestDTO("Tools", "Construction tools");

        Mockito.when(categoryService.saveCategory(any(CategoryRequestDTO.class)))
                .thenReturn(sampleCategory);

        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("Tools")));
    }

    @Test
    void testUpdateCategory() throws Exception {
        CategoryRequestDTO request = new CategoryRequestDTO("Updated Tools", "Updated description");

        Mockito.when(categoryService.putCategory(eq(1L), any(CategoryRequestDTO.class)))
                .thenReturn(sampleCategory);

        mockMvc.perform(put("/api/categories/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Tools")));
    }

    @Test
    void testPatchName() throws Exception {
        Mockito.when(categoryService.pathCategoryName(eq(1L), any(String.class)))
                .thenReturn(sampleCategory);

        mockMvc.perform(patch("/api/categories/1/name")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("\"Updated Tools\""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Tools")));
    }

    @Test
    void testDeleteCategory() throws Exception {
        Mockito.doNothing().when(categoryService).deleteCategoryById(1L);

        mockMvc.perform(delete("/api/categories/1"))
                .andExpect(status().isNoContent());
    }
}