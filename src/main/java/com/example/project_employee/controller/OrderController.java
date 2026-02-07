package com.example.project_employee.controller;

import com.example.project_employee.dto.OrderRequestDto;
import com.example.project_employee.dto.OrderResponseDto;
import com.example.project_employee.dto.PageResponse;
import com.example.project_employee.dto.ProductResponseDto;
import com.example.project_employee.entity.ProductEntity;
import com.example.project_employee.enums.OrderStatus;
import com.example.project_employee.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Orders", description = "API для управления заказами")
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/orders")
    @Operation(summary = "Создать новый заказ", description = "Создает новый заказ")
    public ResponseEntity<OrderResponseDto> addOrder(@Valid @RequestBody OrderRequestDto requestDto) {

        OrderResponseDto responseDto = orderService.addOrder(requestDto);

        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @PostMapping("/orders/{orderId}/products/{productId}")
    @Operation(summary = "Добавить товар в заказ", description = "Позволяет добавить товар в заказ")
    public ResponseEntity<OrderResponseDto> addProductToOrder(@PathVariable("orderId") Long orderId,
                                                              @PathVariable("productId") long productId) {

        OrderResponseDto responseDto = orderService.addProductToOrder(orderId, productId);

        return new ResponseEntity<>(responseDto, HttpStatus.ACCEPTED);
    }

    @GetMapping("/orders")
    @Operation(summary = "Получить все заказы", description = "Возвращает список всех заказов")
    public ResponseEntity<PageResponse<OrderResponseDto>> getAllOrders(
            @RequestParam(required = false) LocalDateTime createdDate,
            @RequestParam(required = false, name = "orderStatus") OrderStatus orderStatus,
            @RequestParam(required = false) Long productId,
            @PageableDefault(page = 0, size = 10, sort = "createdDate")
            Pageable pageable
    ) {

             PageResponse<OrderResponseDto> response = orderService.getAllOrders(
                createdDate,
                orderStatus,
                productId,
                pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/orders/{id}")
    @Operation(summary = "Получить заказ по его ID", description = "Позволяет получить информацию о заказе по его ID")
    public ResponseEntity<OrderResponseDto> getOrderById(@PathVariable("id") Long id) {

        return new ResponseEntity<>(orderService.getOrderById(id), HttpStatus.OK);
    }

    @DeleteMapping("/orders/{id}")
    @Operation(summary = "Удалить заказ по его ID", description = "Позволяет удалить заказ по его ID")
    public ResponseEntity<Long> removeOrder(@PathVariable("id") Long id) {

        Long removedOrder = orderService.removeOrder(id);

        return ResponseEntity.ok(removedOrder);
    }

    @DeleteMapping("/orders/{orderId}/products/{productId}")
    @Operation(summary = "Удалить товар в заказе", description = "Позволяет удалить товар в заказе по ID")
    public ResponseEntity<List<ProductResponseDto>> removeProductInOrder(@PathVariable("orderId") Long orderId,
                                                                         @PathVariable("productId") long productId) {

        List<ProductResponseDto> removedProduct = orderService.removeProductInOrder(orderId, productId);

        return ResponseEntity.ok(removedProduct);
    }

    @PatchMapping("/orders/{id}")
    @Operation(summary = "Обновить заказ по его ID", description = "Позволяет обновить информацию о заказе по его ID")
    public ResponseEntity<OrderResponseDto> updateOrder(@PathVariable("id") Long id,
                                                        @Valid @RequestBody OrderRequestDto requestDto) {

        OrderResponseDto updatedOrder = orderService.updateOrder(id, requestDto);

        return new ResponseEntity<>(updatedOrder, HttpStatus.OK);
    }
}
