package com.example.project_employee.controller;

import com.example.project_employee.dto.EmployeeRequestDto;
import com.example.project_employee.dto.EmployeeResponseDto;
import com.example.project_employee.dto.PageResponse;
import com.example.project_employee.enums.EmployeeRole;
import com.example.project_employee.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Employees", description = "API для управления сотрудниками")
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping("/employees")
    @Operation(summary = "Создать нового сотрудника", description = "Позволяет создать нового сотрудника")
    public ResponseEntity<EmployeeResponseDto> addEmployee(@Valid @RequestBody EmployeeRequestDto employeeRequestDto) {

        EmployeeResponseDto newEmployeeEntity = employeeService.addEmployee(employeeRequestDto);

        return new ResponseEntity<>(newEmployeeEntity, HttpStatus.CREATED);
    }

    @GetMapping("/employees")
    @Operation(summary = "Получить всех сотрудников", description = "Возвращает список всех сотрудников")
    public ResponseEntity<PageResponse<EmployeeResponseDto>> getEmployees(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) EmployeeRole role,
            @RequestParam(required = false) String emailLike,
            @PageableDefault(page = 0, size = 10, sort = "lastName", direction = Sort.Direction.ASC)
            Pageable pageable
    ) {

        PageResponse<EmployeeResponseDto> response = employeeService.getEmployees(
                firstName, lastName, role, emailLike, pageable
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/employees/{id}")
    @Operation(summary = "Получить информацию о сотруднике",
            description = "Позволяет получить информацию о сотруднику по его ID")
    public ResponseEntity<EmployeeResponseDto> getEmployeeById(@PathVariable("id") Long id) {

        return new ResponseEntity<>(employeeService.getEmployeeById(id), HttpStatus.OK);
    }

    @DeleteMapping("/employees/{id}")
    @Operation(summary = "Удалить сотрудника",
            description = "Позволяет удалить сотрудника по его ID")
    public ResponseEntity<Long> removeEmployee(@PathVariable("id") Long id) {

        Long removedEmployeeId = employeeService.removeEmployee(id);

        return ResponseEntity.ok(removedEmployeeId);
    }

    @PatchMapping("/employees/{id}")
    @Operation(summary = "Обновить информацию о сотруднике",
            description = "Позволяет обновить информацию о сотруднику по его ID")
    public ResponseEntity<EmployeeResponseDto> updateEmployee(@PathVariable("id") Long id,
                                                              @Valid
                                                              @RequestBody EmployeeRequestDto employeeRequestDto) {

        EmployeeResponseDto updatedEmployeeEntity = employeeService.updateEmployee(id, employeeRequestDto);

        return new ResponseEntity<>(updatedEmployeeEntity, HttpStatus.OK);
    }
}
