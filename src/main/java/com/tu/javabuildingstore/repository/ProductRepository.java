package com.tu.javabuildingstore.repository;

import com.tu.javabuildingstore.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Long> {

    // filter by category
    @Query("SELECT p FROM Product p WHERE LOWER(p.category.name) = LOWER(:categoryName)")
    List<Product> findByCategoryName(@Param("categoryName") String categoryName);

    // filter by price range
    @Query("SELECT p FROM Product p WHERE p.price BETWEEN :minPrice AND :maxPrice")
    List<Product> findByPriceRange(@Param("minPrice") BigDecimal minPrice,
                                   @Param("maxPrice") BigDecimal maxPrice);

    // filter by products with discount
    @Query("SELECT p FROM Product p WHERE p.discountPercent IS NOT NULL AND p.discountPercent > 0")
    List<Product> findDiscountedProducts();

    // filter by name (not full, case insensitive)
    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Product> searchByName(@Param("keyword") String keyword);
}
