package com.example.project_employee.repository;

import com.example.project_employee.entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    Optional<ProductEntity> findByName(String name);

    boolean existsByName(String name);

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END " +
            "FROM ProductEntity p WHERE p.name = :name AND p.id != :id")
    boolean existsByNameAndIdNot(@Param("name") String name, @Param("id") Long id);

    List<ProductEntity> findByNameContainingIgnoreCase(String name);

    Page<ProductEntity> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);

    @Query("SELECT p FROM ProductEntity p WHERE " +
            "(:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
            "(:minPrice IS NULL OR p.price >= :minPrice) AND " +
            "(:maxPrice IS NULL OR p.price <= :maxPrice)")
    Page<ProductEntity> findByFilter(
            @Param("name") String name,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            Pageable pageable);

    @Query("SELECT p FROM ProductEntity p WHERE p.id IN :ids")
    List<ProductEntity> findByIdIn(@Param("ids") List<Long> ids);

    @Query("SELECT COUNT(oi) FROM OrderItemEntity oi WHERE oi.product.id = :productId")
    long countOrderItemsByProductId(@Param("productId") Long productId);
}