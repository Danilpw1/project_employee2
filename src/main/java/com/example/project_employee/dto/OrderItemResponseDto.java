package com.example.project_employee.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemResponseDto {
    private Long id;
    private ProductResponseDto product;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;
}