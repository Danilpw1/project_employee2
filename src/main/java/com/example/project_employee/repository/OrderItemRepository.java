package com.example.project_employee.repository;

import com.example.project_employee.entity.OrderItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItemEntity, Long> {

    List<OrderItemEntity> findByOrderId(Long orderId);

    @Query("SELECT oi FROM OrderItemEntity oi WHERE oi.order.id = :orderId AND oi.product.id = :productId")
    Optional<OrderItemEntity> findByOrderIdAndProductId(@Param("orderId") Long orderId, @Param("productId") Long productId);

    void deleteByOrderId(Long orderId);

    @Query("SELECT SUM(oi.quantity) FROM OrderItemEntity oi WHERE oi.product.id = :productId")
    Long getTotalQuantitySold(@Param("productId") Long productId);
}