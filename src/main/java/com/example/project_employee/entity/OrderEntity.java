package com.example.project_employee.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private OrderStatus status;

    @Column(name = "total_amount", precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "total_items")
    private Integer totalItems;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private ClientEntity client;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItemEntity> orderItems = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        calculateTotals();
    }

    @PreUpdate
    protected void onUpdate() {
        calculateTotals();
    }

    public void calculateTotals() {
        if (orderItems != null && !orderItems.isEmpty()) {
            totalAmount = orderItems.stream()
                    .map(OrderItemEntity::getTotalPrice)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            totalItems = orderItems.stream()
                    .map(OrderItemEntity::getQuantity)
                    .reduce(0, Integer::sum);
        } else {
            totalAmount = BigDecimal.ZERO;
            totalItems = 0;
        }
    }

    public enum OrderStatus {
        NEW,
        PROCESSING,
        COMPLETED,
        CANCELED
    }
}