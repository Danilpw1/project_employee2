package com.example.project_employee.repository;

import com.example.project_employee.entity.OrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    List<OrderEntity> findByClientId(Long clientId);

    List<OrderEntity> findByStatus(OrderEntity.OrderStatus status);

    List<OrderEntity> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

    Page<OrderEntity> findByClientId(Long clientId, Pageable pageable);

    Page<OrderEntity> findByStatus(OrderEntity.OrderStatus status, Pageable pageable);

    @Query("SELECT o FROM OrderEntity o WHERE " +
            "(:status IS NULL OR o.status = :status) AND " +
            "(:startDate IS NULL OR o.createdAt >= :startDate) AND " +
            "(:endDate IS NULL OR o.createdAt <= :endDate)")
    Page<OrderEntity> findByAllFilters(
            @Param("status") OrderEntity.OrderStatus status,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable);

    @Query("SELECT o FROM OrderEntity o WHERE o.client.id = :clientId AND " +
            "(:status IS NULL OR o.status = :status) AND " +
            "(:startDate IS NULL OR o.createdAt >= :startDate) AND " +
            "(:endDate IS NULL OR o.createdAt <= :endDate)")
    Page<OrderEntity> findByClientAndFilters(
            @Param("clientId") Long clientId,
            @Param("status") OrderEntity.OrderStatus status,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable);
}