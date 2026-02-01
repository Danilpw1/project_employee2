package com.example.project_employee.dto;

import com.example.project_employee.entity.OrderEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDto {
    private Long id;
    private LocalDateTime createdAt;
    private OrderEntity.OrderStatus status;
    private BigDecimal totalAmount;
    private Integer totalItems;
    private ClientSimpleDto client;
    private List<OrderItemResponseDto> orderItems;
}