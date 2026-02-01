package com.example.project_employee.dto;

import com.example.project_employee.entity.OrderEntity;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDto {

    @NotNull(message = "Статус заказа обязателен")
    private OrderEntity.OrderStatus status;

    @NotNull(message = "ID клиента обязательно")
    private Long clientId;
}