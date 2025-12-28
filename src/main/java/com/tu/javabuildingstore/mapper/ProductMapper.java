package com.tu.javabuildingstore.mapper;

import com.tu.javabuildingstore.dto.category.CategoryResponseDTO;
import com.tu.javabuildingstore.dto.product.ProductRequestDTO;
import com.tu.javabuildingstore.dto.product.ProductResponseDTO;
import com.tu.javabuildingstore.model.Category;
import com.tu.javabuildingstore.model.Product;
import org.springframework.stereotype.Component;

@Component
public interface ProductMapper {

    ProductResponseDTO toDto(Product product);

    Product toEntity(ProductRequestDTO dto);

    Product toEntity(ProductRequestDTO dto, Category category);

    Product updateFromDto(Product existing, ProductRequestDTO dto, Category category);

    @Component
    class ProductMapperImpl implements ProductMapper {

        private final CategoryMapper categoryMapper;

        public ProductMapperImpl(CategoryMapper categoryMapper) {
            this.categoryMapper = categoryMapper;
        }

        @Override
        public ProductResponseDTO toDto(Product product) {
            if (product == null) return null;

            CategoryResponseDTO categoryDto = product.getCategory() == null
                    ? null
                    : categoryMapper.toDto(product.getCategory());

            return new ProductResponseDTO(
                    product.getId(),
                    product.getName(),
                    product.getDescription(),
                    product.getPrice(),
                    product.getDiscountPercent(),
                    product.getDiscountedPrice(),
                    product.getStockQuantity(),
                    product.getImageUrl(),
                    categoryDto
            );
        }

        @Override
        public Product toEntity(ProductRequestDTO dto) {
            if (dto == null) return null;
            return Product.builder()
                    .name(dto.name())
                    .description(dto.description())
                    .price(dto.price())
                    .discountPercent(dto.discountPercent())
                    .stockQuantity(dto.stockQuantity())
                    .imageUrl(dto.imageUrl())
                    .build();
        }

        @Override
        public Product toEntity(ProductRequestDTO dto, Category category) {
            Product p = toEntity(dto);
            if (p != null) {
                p.setCategory(category);
            }
            return p;
        }

        @Override
        public Product updateFromDto(Product existing, ProductRequestDTO dto, Category category) {
            if (existing == null || dto == null) return existing;
            existing.setName(dto.name());
            existing.setDescription(dto.description());
            existing.setPrice(dto.price());
            existing.setDiscountPercent(dto.discountPercent());
            existing.setStockQuantity(dto.stockQuantity());
            existing.setImageUrl(dto.imageUrl());
            existing.setCategory(category); // single category
            return existing;
        }
    }
}
