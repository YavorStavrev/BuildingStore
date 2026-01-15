package com.training.building_store.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.training.building_store.dto.product.*;
import com.training.building_store.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

//    @MockBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    private ProductResponseDTO sampleProduct;

    @BeforeEach
    void setUp() {
        sampleProduct = new ProductResponseDTO(
                1L,
                "Hammer",
                "Heavy duty hammer",
                new BigDecimal("50"),
                10.0,
                new BigDecimal("45.0"),
                100,
                null,
                null
        );
    }

    @Test
    void testListAll() throws Exception {
        Mockito.when(productService.listAll()).thenReturn(List.of(sampleProduct));

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is("Hammer")))
                .andExpect(jsonPath("$[0].price", is(50)));
    }

    @Test
    void testGetProduct() throws Exception {
        Mockito.when(productService.getProduct(1L)).thenReturn(sampleProduct);

        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Hammer")))
                .andExpect(jsonPath("$.discountedPrice", is(45.0)));
    }

    @Test
    void testCreateProduct() throws Exception {
        ProductRequestDTO request = new ProductRequestDTO(
                "Hammer",
                "Heavy duty hammer",
                new BigDecimal("50"),
                10.0,
                100,
                null,
                1L
        );

        Mockito.when(productService.createProduct(any(ProductRequestDTO.class)))
                .thenReturn(sampleProduct);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("Hammer")));
    }

    @Test
    void testUpdateProduct() throws Exception {
        ProductRequestDTO request = new ProductRequestDTO(
                "Hammer Updated",
                "Updated description",
                new BigDecimal("60"),
                5.0,
                90,
                null,
                1L
        );

        ProductResponseDTO updatedProduct = new ProductResponseDTO(
                1L, "Hammer Updated", "Updated description", new BigDecimal("60"), 5.0,
                new BigDecimal("57.0"), 90, null, null
        );

        Mockito.when(productService.updateProduct(eq(1L), any(ProductRequestDTO.class)))
                .thenReturn(updatedProduct);

        mockMvc.perform(put("/api/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Hammer Updated")))
                .andExpect(jsonPath("$.discountedPrice", is(57.0)));
    }

    @Test
    void testUpdateProductPrice() throws Exception {
        ProductPriceUpdateDTO dto = new ProductPriceUpdateDTO(new BigDecimal("70"));
        ProductResponseDTO updated = new ProductResponseDTO(
                1L, "Hammer", "Heavy duty hammer", new BigDecimal("70"), 10.0,
                new BigDecimal("63.0"), 100, null, null
        );

        Mockito.when(productService.updatePrice(eq(1L), any(ProductPriceUpdateDTO.class)))
                .thenReturn(updated);

        mockMvc.perform(patch("/api/products/1/price")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.price", is(70)));
    }

    @Test
    void testUpdateProductDiscount() throws Exception {
        ProductDiscountUpdateDTO dto = new ProductDiscountUpdateDTO(20.0);
        ProductResponseDTO updated = new ProductResponseDTO(
                1L, "Hammer", "Heavy duty hammer", new BigDecimal("50"), 20.0,
                new BigDecimal("40.0"), 100, null, null
        );

        Mockito.when(productService.updateDiscount(eq(1L), any(ProductDiscountUpdateDTO.class)))
                .thenReturn(updated);

        mockMvc.perform(patch("/api/products/1/discount")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.discountPercent", is(20.0)))
                .andExpect(jsonPath("$.discountedPrice", is(40.0)));
    }

    @Test
    void testUpdateProductQuantity() throws Exception {
        ProductQuantityUpdateDTO dto = new ProductQuantityUpdateDTO(150);
        ProductResponseDTO updated = new ProductResponseDTO(
                1L, "Hammer", "Heavy duty hammer", new BigDecimal("50"), 10.0,
                new BigDecimal("45.0"), 150, null, null
        );

        Mockito.when(productService.updateStockQuantity(eq(1L), any(ProductQuantityUpdateDTO.class)))
                .thenReturn(updated);

        mockMvc.perform(patch("/api/products/1/quantity")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stockQuantity", is(150)));
    }

    @Test
    void testDeleteProduct() throws Exception {
        Mockito.doNothing().when(productService).deleteProduct(1L);

        mockMvc.perform(delete("/api/products/1"))
                .andExpect(status().isNoContent());
    }
}
