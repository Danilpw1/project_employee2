package com.example.project_employee.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemRequestDto {

    @NotNull(message = "ID товара обязательно")
    private Long productId;

    @NotNull(message = "Количество обязательно")
    @Positive(message = "Количество должно быть положительным числом")
    private Integer quantity;
}