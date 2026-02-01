package com.example.project_employee.mapper;

import com.example.project_employee.dto.*;
import com.example.project_employee.entity.ClientEntity;
import com.example.project_employee.entity.OrderEntity;
import com.example.project_employee.entity.OrderItemEntity;
import com.example.project_employee.entity.ProductEntity;
import org.mapstruct.*;

import java.math.BigDecimal;
import java.util.List;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderMapper {

    OrderResponseDto toResponseDto(OrderEntity orderEntity);

    OrderSimpleDto toSimpleDto(OrderEntity orderEntity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "totalAmount", ignore = true)
    @Mapping(target = "totalItems", ignore = true)
    @Mapping(target = "client", ignore = true)
    @Mapping(target = "orderItems", ignore = true)
    OrderEntity toEntity(OrderRequestDto requestDto);

    default OrderItemResponseDto toOrderItemResponseDto(OrderItemEntity orderItem) {
        if (orderItem == null) {
            return null;
        }

        OrderItemResponseDto dto = new OrderItemResponseDto();
        dto.setId(orderItem.getId());
        dto.setQuantity(orderItem.getQuantity());
        dto.setUnitPrice(orderItem.getUnitPrice());
        dto.setTotalPrice(orderItem.getTotalPrice());

        if (orderItem.getProduct() != null) {
            ProductEntity product = orderItem.getProduct();
            dto.setProduct(new ProductResponseDto(
                    product.getId(),
                    product.getName(),
                    product.getDescription(),
                    product.getPrice(),
                    product.getCreatedAt(),
                    product.getUpdatedAt()
            ));
        }

        return dto;
    }

    default List<OrderItemResponseDto> toOrderItemResponseDtoList(List<OrderItemEntity> orderItems) {
        if (orderItems == null) {
            return null;
        }

        return orderItems.stream()
                .map(this::toOrderItemResponseDto)
                .toList();
    }
}