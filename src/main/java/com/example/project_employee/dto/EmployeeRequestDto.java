package com.example.project_employee.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeRequestDto {

    @NotBlank(message = "Имя обязательно")
    @Size(min = 2, max = 50, message = "Имя должно быть от 2 до 50 символов")
    private String firstName;

    @NotBlank(message = "Фамилия обязательна")
    @Size(min = 2, max = 50, message = "Фамилия должна быть от 2 до 50 символов")
    private String lastName;

    @NotBlank(message = "Должность обязательна")
    @Size(min = 2, max = 100, message = "Должность должна быть от 2 до 100 символов")
    private String position;

    @NotNull(message = "Зарплата обязательна")
    @DecimalMin(value = "0.0", inclusive = false, message = "Зарплата должна быть больше 0")
    @DecimalMax(value = "1000000.0", message = "Зарплата не может превышать 1 000 000")
    private BigDecimal salary;

    @NotBlank(message = "Отдел обязателен")
    @Size(min = 2, max = 100, message = "Отдел должен быть от 2 до 100 символов")
    private String department;

    @NotNull(message = "Дата приема обязательна")
    @PastOrPresent(message = "Дата приема не может быть будущей")
    private LocalDate hireDate;

    @NotBlank(message = "Email обязателен")
    @Email(message = "Некорректный формат email")
    private String email;

    @NotBlank(message = "Телефон обязателен")
    @Pattern(regexp = "^\\+?[0-9\\s\\-\\(\\)]{7,20}$",
            message = "Некорректный формат телефона")
    private String phone;
}