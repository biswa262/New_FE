package com.example.e_comerce.repository;

import com.example.e_comerce.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface ProductRepository extends JpaRepository<Product,Long> {

@Query("SELECT p FROM Product p WHERE " +
       "((:category IS NULL OR :category = '') OR p.category.name = :category) AND " +
       "((:minPrice IS NULL AND :maxPrice IS NULL) OR (p.discountedPrice BETWEEN :minPrice AND :maxPrice)) AND " +
       "(:minDiscount IS NULL OR p.discountedPercent >= :minDiscount) " +
       "ORDER BY CASE WHEN :sort = 'price_low' THEN p.discountedPrice END ASC, " +
       "CASE WHEN :sort = 'price_high' THEN p.discountedPrice END DESC, p.createdAt DESC")
List<Product> filterProducts(@Param("category") String category,
                             @Param("minPrice") Integer minPrice,
                             @Param("maxPrice") Integer maxPrice,
                             @Param("minDiscount") Integer minDiscount,
                             @Param("sort") String sort);


    @Query("SELECT p FROM Product p WHERE " +
            "LOWER(p.title) LIKE %:query% OR " +
            "LOWER(p.description) LIKE %:query% OR " +
            "LOWER(p.brand) LIKE %:query% OR " +
            "LOWER(p.category.name) LIKE %:query% OR " +
            "LOWER(p.category.parentCategory.name) LIKE %:query% OR " +
            "LOWER(p.category.parentCategory.parentCategory.name) LIKE %:query%")
    List<Product> searchProduct(@Param("query") String query);


    @Query("SELECT p FROM Product p " +
            "WHERE LOWER(p.category.name) = :category " +
            "OR LOWER(p.category.parentCategory.name) = :category " +
            "OR LOWER(p.category.parentCategory.parentCategory.name) = :category")
    List<Product> findByCategory(@Param("category") String category);


    @Query("SELECT p FROM Product p WHERE p.category.id IN :categoryIds")
    List<Product> findByCategoryIds(@Param("categoryIds") List<Long> categoryIds);


    public List<Product> findTop10ByOrderByCreatedAtDesc();

}