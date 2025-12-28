package com.tu.javabuildingstore.service;

import com.tu.javabuildingstore.dto.product.*;
import com.tu.javabuildingstore.exception.ResourceNotFoundException;
import com.tu.javabuildingstore.mapper.ProductMapper;
import com.tu.javabuildingstore.model.Category;
import com.tu.javabuildingstore.model.Product;
import com.tu.javabuildingstore.repository.CategoryRepository;
import com.tu.javabuildingstore.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    public ProductService(ProductRepository productRepository,
                          CategoryRepository categoryRepository,
                          ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.productMapper = productMapper;
    }

    @Transactional
    public ProductResponseDTO createProduct(ProductRequestDTO dto) {
        Category category = resolveCategory(dto.categoryId());
        Product product = productMapper.toEntity(dto, category);

        product.setImageUrl(dto.imageUrl());

        Product saved = productRepository.save(product);
        return productMapper.toDto(saved);
    }

    @Transactional
    public ProductResponseDTO updateProduct(Long id, ProductRequestDTO dto) {
        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Product.class, id));

        Category category = resolveCategory(dto.categoryId());
        productMapper.updateFromDto(existing, dto, category);

        existing.setImageUrl(dto.imageUrl());

        Product saved = productRepository.save(existing);
        return productMapper.toDto(saved);
    }

    @Transactional(readOnly = true)
    public ProductResponseDTO getProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Product.class, id));
        return productMapper.toDto(product);
    }

    @Transactional(readOnly = true)
    public List<ProductResponseDTO> listAll() {
        return productRepository.findAll()
                .stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Product.class, id));
        productRepository.delete(product);
    }

    @Transactional
    public ProductResponseDTO updatePrice(Long id, ProductPriceUpdateDTO dto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Product.class, id));

        product.setPrice(dto.price());
        Product saved = productRepository.save(product);
        return productMapper.toDto(saved);
    }

    @Transactional
    public ProductResponseDTO updateDiscount(Long id, ProductDiscountUpdateDTO dto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Product.class, id));

        product.setDiscountPercent(dto.discountPercent());
        Product saved = productRepository.save(product);
        return productMapper.toDto(saved);
    }

    @Transactional
    public ProductResponseDTO updateStockQuantity(Long id, ProductQuantityUpdateDTO dto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Product.class, id));

        product.setStockQuantity(dto.stockQuantity());
        Product saved = productRepository.save(product);
        return productMapper.toDto(saved);
    }

    public List<ProductResponseDTO> listFiltered(String categoryName,
                                                 BigDecimal minPrice,
                                                 BigDecimal maxPrice,
                                                 Boolean discounted,
                                                 String keyword) {
        List<Product> products;

        if (categoryName != null && !categoryName.isBlank()) {
            products = productRepository.findByCategoryName(categoryName);
        } else if (minPrice != null && maxPrice != null) {
            products = productRepository.findByPriceRange(minPrice, maxPrice);
        } else if (Boolean.TRUE.equals(discounted)) {
            products = productRepository.findDiscountedProducts();
        } else if (keyword != null && !keyword.isBlank()) {
            products = productRepository.searchByName(keyword);
        } else {
            products = productRepository.findAll();
        }

        return products.stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());
    }

    // --- Helper Method ---

    private Category resolveCategory(Long categoryId) {
        if (categoryId == null) {
            throw new IllegalArgumentException("Category ID must not be null");
        }
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException(Category.class, categoryId));
    }
}
