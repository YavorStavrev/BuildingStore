package com.tu.javabuildingstore.controller;

import com.tu.javabuildingstore.dto.product.*;
import com.tu.javabuildingstore.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> listAll() {
        return ResponseEntity.ok(productService.listAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getProduct(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProduct(id));
    }

    @GetMapping("/filtered")
    public ResponseEntity<List<ProductResponseDTO>> listFiltered(
            @RequestParam(name = "category", required = false) String categoryName,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) Boolean discounted,
            @RequestParam(name = "q", required = false) String keyword) {

        return ResponseEntity.ok(productService.listFiltered(categoryName, minPrice, maxPrice, discounted, keyword));
    }

    @GetMapping("/by-category")
    public ResponseEntity<List<ProductResponseDTO>> listByCategory(
            @RequestParam(name = "category") String categoryName) {

        return ResponseEntity.ok(productService.listFiltered(categoryName, null, null, null, null));
    }

    @GetMapping("/by-price-range")
    public ResponseEntity<List<ProductResponseDTO>> listByPriceRange(
            @RequestParam(name = "minPrice") BigDecimal minPrice,
            @RequestParam(name = "maxPrice") BigDecimal maxPrice) {

        return ResponseEntity.ok(productService.listFiltered(null, minPrice, maxPrice, null, null));
    }

    @GetMapping("/discounted")
    public ResponseEntity<List<ProductResponseDTO>> listDiscounted() {
        return ResponseEntity.ok(productService.listFiltered(null, null, null, true, null));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductResponseDTO>> searchByName(
            @RequestParam(name = "q") String keyword) {

        return ResponseEntity.ok(productService.listFiltered(null, null, null, null, keyword));
    }

    @PostMapping
    public ResponseEntity<ProductResponseDTO> createProduct(@RequestBody @Valid ProductRequestDTO product) {
        ProductResponseDTO created = productService.createProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> updateProduct(@PathVariable Long id,
                                                            @RequestBody @Valid ProductRequestDTO product) {
        ProductResponseDTO updated = productService.updateProduct(id, product);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{id}/price")
    public ResponseEntity<ProductResponseDTO> updateProductPrice(
            @PathVariable Long id,
            @RequestBody @Valid ProductPriceUpdateDTO dto) {
        ProductResponseDTO updated = productService.updatePrice(id, dto);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{id}/discount")
    public ResponseEntity<ProductResponseDTO> updateProductDiscount(
            @PathVariable Long id,
            @RequestBody @Valid ProductDiscountUpdateDTO dto) {
        ProductResponseDTO updated = productService.updateDiscount(id, dto);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{id}/quantity")
    public ResponseEntity<ProductResponseDTO> updateProductQuantity(
            @PathVariable Long id,
            @RequestBody @Valid ProductQuantityUpdateDTO dto) {
        ProductResponseDTO updated = productService.updateStockQuantity(id, dto);
        return ResponseEntity.ok(updated);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}