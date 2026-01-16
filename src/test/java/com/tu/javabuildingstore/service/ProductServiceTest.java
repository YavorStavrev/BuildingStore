package com.tu.javabuildingstore.service;

import com.tu.javabuildingstore.dto.product.ProductPriceUpdateDTO;
import com.tu.javabuildingstore.dto.product.ProductRequestDTO;
import com.tu.javabuildingstore.dto.product.ProductResponseDTO;
import com.tu.javabuildingstore.exception.ResourceNotFoundException;
import com.tu.javabuildingstore.mapper.ProductMapper;
import com.tu.javabuildingstore.model.Category;
import com.tu.javabuildingstore.model.Product;
import com.tu.javabuildingstore.repository.CategoryRepository;
import com.tu.javabuildingstore.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ProductServiceTest {

    private ProductRepository productRepository;
    private CategoryRepository categoryRepository;
    private ProductMapper productMapper;
    private ProductService productService;

    private Category category;
    private Product product;

    @BeforeEach
    void setUp() {
        productRepository = mock(ProductRepository.class);
        categoryRepository = mock(CategoryRepository.class);
        productMapper = mock(ProductMapper.class);

        productService = new ProductService(productRepository, categoryRepository, productMapper);

        category = Category.builder().id(1L).name("Tools").build();
        product = Product.builder()
                .id(1L)
                .name("Hammer")
                .description("Heavy duty hammer")
                .price(new BigDecimal("50"))
                .discountPercent(10.0)
                .stockQuantity(100)
                .category(category)
                .build();
    }

    @Test
    void testCreateProduct_Success() {
        ProductRequestDTO request = new ProductRequestDTO(
                "Hammer",
                "Heavy duty hammer",
                new BigDecimal("50"),
                10.0,
                100,
                null,
                1L
        );

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(productMapper.toEntity(request, category)).thenReturn(product);
        when(productRepository.save(product)).thenReturn(product);
        when(productMapper.toDto(product)).thenReturn(new ProductResponseDTO(
                1L, "Hammer", "Heavy duty hammer", new BigDecimal("50"), 10.0,
                new BigDecimal("45.0"), 100, null, null
        ));

        ProductResponseDTO response = productService.createProduct(request);

        assertNotNull(response);
        assertEquals("Hammer", response.name());
        verify(productRepository).save(product);
    }

    @Test
    void testGetProduct_NotFound() {
        when(productRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> productService.getProduct(2L));
    }

    @Test
    void testDeleteProduct_Success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        productService.deleteProduct(1L);

        verify(productRepository).delete(product);
    }

    @Test
    void testUpdatePrice_Success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        ProductPriceUpdateDTO dto = new ProductPriceUpdateDTO(new BigDecimal("60"));

        when(productRepository.save(product)).thenReturn(product);
        when(productMapper.toDto(product)).thenReturn(new ProductResponseDTO(
                1L, "Hammer", "Heavy duty hammer", new BigDecimal("60"), 10.0,
                new BigDecimal("54.0"), 100, null, null
        ));

        ProductResponseDTO response = productService.updatePrice(1L, dto);

        assertEquals(new BigDecimal("60"), response.price());
    }

    @Test
    void testResolveCategory_NotFound() {
        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());
        ProductRequestDTO request = new ProductRequestDTO(
                "Saw", "Hand saw", new BigDecimal("20"), 0.0, 10, null, 99L
        );

        assertThrows(ResourceNotFoundException.class, () -> productService.createProduct(request));
    }

    @Test
    void testListAll() {
        when(productRepository.findAll()).thenReturn(List.of(product));
        when(productMapper.toDto(product)).thenReturn(new ProductResponseDTO(
                1L, "Hammer", "Heavy duty hammer", new BigDecimal("50"), 10.0,
                new BigDecimal("45.0"), 100, null, null
        ));

        List<ProductResponseDTO> all = productService.listAll();

        assertEquals(1, all.size());
        assertEquals("Hammer", all.get(0).name());
    }
}
