package com.example.project_employee.controller;

import com.example.project_employee.dto.CustomerRequestDto;
import com.example.project_employee.dto.CustomerResponseDto;
import com.example.project_employee.dto.PageResponse;
import com.example.project_employee.service.CustomerService;
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
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Клиенты", description = "API для управления клиентами")
public class CustomerController {
    private final CustomerService customerService;

    @PostMapping("/customers")
    @Operation(summary = "Добавить клиента", description = "Позволяет добавить нового клиента")
    public ResponseEntity<CustomerResponseDto> addCustomer(@Valid @RequestBody CustomerRequestDto customerRequestDto) {

        CustomerResponseDto responseDto = customerService.addCustomer(customerRequestDto);

        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @GetMapping("/customers")
    @Operation(summary = "Показать всех клиентов", description = "Возвращает список всех клиентов")
    public ResponseEntity<PageResponse<CustomerResponseDto>> getCustomers(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String emailLike,
            @RequestParam(required = false) String phoneNumber,
            @PageableDefault(page = 0, size = 10, sort = "firstName", direction = Sort.Direction.ASC)
            Pageable pageable
    ) {

        PageResponse<CustomerResponseDto> response = customerService.getAllCustomers(
                firstName, lastName, emailLike, phoneNumber, pageable
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/customers/{id}")
    @Operation(summary = "Показать клиента по его ID", description = "Возвращает информацию о клиенте по его ID")
    public ResponseEntity<CustomerResponseDto> getCustomerById(@PathVariable("id") Long id) {

        return new ResponseEntity<>(customerService.getCustomerById(id), HttpStatus.OK);
    }

    @DeleteMapping("/customers/{id}")
    @Operation(summary = "Удалить клиента по его ID",
            description = "Позволяет удалить клиента по его ID")
    public ResponseEntity<Long> removeCustomer(@PathVariable("id") Long id) {

        long removedCustomer = customerService.removeCustomer(id);

        return ResponseEntity.ok(removedCustomer);
    }

    @PatchMapping("/customers/{id}")
    @Operation(summary = "Обновить данные клиента по его ID",
            description = "Позволяет удалить клиента по его ID")
    public ResponseEntity<CustomerResponseDto> updateCustomer(@PathVariable("id") Long id,
                                                              @Valid @RequestBody CustomerRequestDto requestDto) {

        CustomerResponseDto updatedCustomerEntity = customerService.updateCustomer(id, requestDto);

        return new ResponseEntity<>(updatedCustomerEntity, HttpStatus.OK);
    }
}
