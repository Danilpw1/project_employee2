package com.example.project_employee.service;

import com.example.project_employee.dto.OrderRequestDto;
import com.example.project_employee.dto.OrderResponseDto;
import com.example.project_employee.entity.ClientEntity;
import com.example.project_employee.entity.OrderEntity;
import com.example.project_employee.mapper.OrderMapper;
import com.example.project_employee.repository.ClientRepository;
import com.example.project_employee.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final ClientRepository clientRepository;
    private final OrderMapper orderMapper;

    public OrderResponseDto createOrder(OrderRequestDto requestDto) {
        log.info("Создание нового заказа для клиента с ID: {}", requestDto.getClientId());

        ClientEntity clientEntity = clientRepository.findById(requestDto.getClientId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Клиент с ID " + requestDto.getClientId() + " не найден"
                ));

        OrderEntity orderEntity = orderMapper.toEntityWithClient(requestDto, clientEntity);
        OrderEntity saved = orderRepository.save(orderEntity);
        log.info("Создан заказ с ID: {}", saved.getId());

        return orderMapper.toResponseDto(saved);
    }

    @Transactional(readOnly = true)
    public OrderResponseDto getOrderById(Long id) {
        log.info("Получение заказа по ID: {}", id);

        OrderEntity orderEntity = orderRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Заказ с ID " + id + " не найден"
                ));

        return orderMapper.toResponseDto(orderEntity);
    }

    public OrderResponseDto updateOrder(Long id, OrderRequestDto requestDto) {
        log.info("Обновление заказа с ID: {}", id);

        OrderEntity orderEntity = orderRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Заказ с ID " + id + " не найден"
                ));

        orderMapper.updateEntity(orderEntity, requestDto);

        // Обновляем клиента если изменился clientId
        if (!orderEntity.getClient().getId().equals(requestDto.getClientId())) {
            ClientEntity clientEntity = clientRepository.findById(requestDto.getClientId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Клиент с ID " + requestDto.getClientId() + " не найден"
                    ));
            orderEntity.setClient(clientEntity);
        }

        OrderEntity updated = orderRepository.save(orderEntity);
        log.info("Обновлен заказ с ID: {}", updated.getId());

        return orderMapper.toResponseDto(updated);
    }

    public void deleteOrder(Long id) {
        log.info("Удаление заказа с ID: {}", id);

        if (!orderRepository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Заказ с ID " + id + " не найден"
            );
        }

        orderRepository.deleteById(id);
        log.info("Удален заказ с ID: {}", id);
    }

    @Transactional(readOnly = true)
    public Page<OrderResponseDto> getAllOrders(Pageable pageable) {
        log.info("Получение всех заказов с пагинацией: page={}, size={}",
                pageable.getPageNumber(), pageable.getPageSize());

        return orderRepository.findAll(pageable)
                .map(orderMapper::toResponseDto);
    }

    @Transactional(readOnly = true)
    public List<OrderResponseDto> getAllOrders() {
        log.info("Получение всех заказов без пагинации");

        return orderRepository.findAll().stream()
                .map(orderMapper::toResponseDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public Page<OrderResponseDto> getOrdersWithFilters(
            OrderEntity.OrderStatus status,
            LocalDateTime startDate,
            LocalDateTime endDate,
            Pageable pageable) {

        log.info("Фильтрация заказов с параметрами: status={}, startDate={}, endDate={}",
                status, startDate, endDate);

        Page<OrderEntity> orderEntities = orderRepository.findByAllFilters(
                status,
                startDate,
                endDate,
                pageable
        );

        return orderEntities.map(orderMapper::toResponseDto);
    }

    @Transactional(readOnly = true)
    public List<OrderResponseDto> getOrdersByClientId(Long clientId) {
        log.info("Получение заказов клиента с ID: {}", clientId);

        if (!clientRepository.existsById(clientId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Клиент с ID " + clientId + " не найден"
            );
        }

        return orderRepository.findByClientId(clientId).stream()
                .map(orderMapper::toResponseDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public Page<OrderResponseDto> getOrdersByClientIdWithFilters(
            Long clientId,
            OrderEntity.OrderStatus status,
            LocalDateTime startDate,
            LocalDateTime endDate,
            Pageable pageable) {

        log.info("Фильтрация заказов клиента {} с параметрами: status={}, startDate={}, endDate={}",
                clientId, status, startDate, endDate);

        if (!clientRepository.existsById(clientId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Клиент с ID " + clientId + " не найден"
            );
        }

        Page<OrderEntity> orderEntities = orderRepository.findByClientAndFilters(
                clientId,
                status,
                startDate,
                endDate,
                pageable
        );

        return orderEntities.map(orderMapper::toResponseDto);
    }

    @Transactional(readOnly = true)
    public List<OrderResponseDto> getOrdersByStatus(OrderEntity.OrderStatus status) {
        log.info("Получение заказов по статусу: {}", status);

        return orderRepository.findByStatus(status).stream()
                .map(orderMapper::toResponseDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<OrderResponseDto> getOrdersByDateRange(LocalDateTime from, LocalDateTime to) {
        log.info("Получение заказов по диапазону дат: {} - {}", from, to);

        if (from == null || to == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Даты начала и конца диапазона должны быть указаны"
            );
        }

        if (from.isAfter(to)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Дата начала не может быть позже даты окончания"
            );
        }

        return orderRepository.findByCreatedAtBetween(from, to).stream()
                .map(orderMapper::toResponseDto)
                .toList();
    }

    public OrderResponseDto updateOrderStatus(Long id, OrderEntity.OrderStatus status) {
        log.info("Обновление статуса заказа {} на {}", id, status);

        OrderEntity orderEntity = orderRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Заказ с ID " + id + " не найден"
                ));

        orderEntity.setStatus(status);
        OrderEntity updated = orderRepository.save(orderEntity);
        log.info("Обновлен статус заказа с ID: {}", updated.getId());

        return orderMapper.toResponseDto(updated);
    }
}