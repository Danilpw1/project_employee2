package com.example.project_employee.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequestDto {

    @NotBlank(message = "Название товара обязательно")
    @Size(min = 2, max = 200, message = "Название должно быть от 2 до 200 символов")
    private String name;

    @Size(max = 1000, message = "Описание не должно превышать 1000 символов")
    private String description;

    @NotNull(message = "Цена обязательна")
    @DecimalMin(value = "0.01", message = "Цена должна быть больше 0")
    @DecimalMax(value = "1000000.0", message = "Цена не может превышать 1 000 000")
    private BigDecimal price;
}