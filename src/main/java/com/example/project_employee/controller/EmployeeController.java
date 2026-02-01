package com.example.project_employee.controller;

import com.example.project_employee.dto.EmployeeRequestDto;
import com.example.project_employee.dto.EmployeeResponseDto;
import com.example.project_employee.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping
    public ResponseEntity<EmployeeResponseDto> createEmployee(@Valid @RequestBody EmployeeRequestDto requestDto) {
        EmployeeResponseDto created = employeeService.createEmployee(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponseDto> getEmployee(@PathVariable Long id) {
        EmployeeResponseDto employee = employeeService.getEmployeeById(id);
        return ResponseEntity.ok(employee);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeResponseDto> updateEmployee(
            @PathVariable Long id,
            @Valid @RequestBody EmployeeRequestDto requestDto) {
        EmployeeResponseDto updated = employeeService.updateEmployee(id, requestDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<EmployeeResponseDto>> getAllEmployees(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<EmployeeResponseDto> employees = employeeService.getAllEmployees(pageable);
        return ResponseEntity.ok(employees);
    }

    @GetMapping("/all")
    public ResponseEntity<List<EmployeeResponseDto>> getAllEmployeesWithoutPagination() {
        List<EmployeeResponseDto> employees = employeeService.getAllEmployees();
        return ResponseEntity.ok(employees);
    }

    @GetMapping("/filter")
    public ResponseEntity<Page<EmployeeResponseDto>> getEmployeesWithFilters(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String department,
            @RequestParam(required = false) String position,
            @RequestParam(required = false) BigDecimal minSalary,
            @RequestParam(required = false) BigDecimal maxSalary,
            @RequestParam(required = false) LocalDate hireDateFrom,
            @RequestParam(required = false) LocalDate hireDateTo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<EmployeeResponseDto> employees = employeeService.getEmployeesWithFilters(
                firstName,
                lastName,
                department,
                position,
                minSalary,
                maxSalary,
                hireDateFrom,
                hireDateTo,
                pageable
        );

        return ResponseEntity.ok(employees);
    }

    @GetMapping("/department/{department}")
    public ResponseEntity<List<EmployeeResponseDto>> getEmployeesByDepartment(
            @PathVariable String department) {
        List<EmployeeResponseDto> employees = employeeService.getEmployeesByDepartment(department);
        return ResponseEntity.ok(employees);
    }

    @GetMapping("/position/{position}")
    public ResponseEntity<List<EmployeeResponseDto>> getEmployeesByPosition(
            @PathVariable String position) {
        List<EmployeeResponseDto> employees = employeeService.getEmployeesByPosition(position);
        return ResponseEntity.ok(employees);
    }

    @GetMapping("/salary-range")
    public ResponseEntity<List<EmployeeResponseDto>> getEmployeesBySalaryRange(
            @RequestParam BigDecimal minSalary,
            @RequestParam BigDecimal maxSalary) {
        List<EmployeeResponseDto> employees = employeeService.getEmployeesBySalaryRange(minSalary, maxSalary);
        return ResponseEntity.ok(employees);
    }

    @GetMapping("/hire-date-range")
    public ResponseEntity<List<EmployeeResponseDto>> getEmployeesByHireDateRange(
            @RequestParam LocalDate from,
            @RequestParam LocalDate to) {
        List<EmployeeResponseDto> employees = employeeService.getEmployeesByHireDateRange(from, to);
        return ResponseEntity.ok(employees);
    }

    @GetMapping("/search")
    public ResponseEntity<List<EmployeeResponseDto>> searchEmployeesByName(
            @RequestParam String name) {
        List<EmployeeResponseDto> employees = employeeService.searchEmployeesByName(name);
        return ResponseEntity.ok(employees);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<EmployeeResponseDto> getEmployeeByEmail(@PathVariable String email) {
        EmployeeResponseDto employee = employeeService.getEmployeeByEmail(email);
        return ResponseEntity.ok(employee);
    }

    @GetMapping("/check-email/{email}")
    public ResponseEntity<Boolean> checkEmailExists(@PathVariable String email) {
        boolean exists = employeeService.existsByEmail(email);
        return ResponseEntity.ok(exists);
    }
}