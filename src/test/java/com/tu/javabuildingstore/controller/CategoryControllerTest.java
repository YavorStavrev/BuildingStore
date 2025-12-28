package com.tu.javabuildingstore.controller;

import com.tu.javabuildingstore.dto.category.CategoryRequestDTO;
import com.tu.javabuildingstore.dto.category.CategoryResponseDTO;
import com.tu.javabuildingstore.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategoryController.class)
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService service;

    @Test
    void getAll_returnsOk() throws Exception {
        when(service.getAllCategories()).thenReturn(List.of(new CategoryResponseDTO()));
        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk());
    }

    @Test
    void getById_returnsOk() throws Exception {
        when(service.getCategoryOrThrow(1L)).thenReturn(new CategoryResponseDTO());
        mockMvc.perform(get("/api/categories/1"))
                .andExpect(status().isOk());
    }

    @Test
    void create_returnsCreated() throws Exception {
        when(service.saveCategory(new CategoryRequestDTO())).thenReturn(new CategoryResponseDTO());
        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isCreated());
    }

    @Test
    void update_returnsOk() throws Exception {
        when(service.putCategory(1L, new CategoryRequestDTO())).thenReturn(new CategoryResponseDTO());
        mockMvc.perform(put("/api/categories/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk());
    }

    @Test
    void patchName_returnsOk() throws Exception {
        when(service.pathCategoryName(1L, "New")).thenReturn(new CategoryResponseDTO());
        mockMvc.perform(patch("/api/categories/1/name")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("\"New\""))
                .andExpect(status().isOk());
    }

    @Test
    void delete_returnsNoContent() throws Exception {
        mockMvc.perform(delete("/api/categories/1"))
                .andExpect(status().isNoContent());
    }
}