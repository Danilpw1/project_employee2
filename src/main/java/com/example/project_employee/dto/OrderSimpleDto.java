package com.example.project_employee.dto;

import com.example.project_employee.entity.OrderEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderSimpleDto {
    private Long id;
    private LocalDateTime createdAt;
    private OrderEntity.OrderStatus status;
    private Long clientId;
}