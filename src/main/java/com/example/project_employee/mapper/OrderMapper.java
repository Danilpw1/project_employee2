package com.example.project_employee.mapper;

import com.example.project_employee.dto.OrderRequestDto;
import com.example.project_employee.dto.OrderResponseDto;
import com.example.project_employee.dto.ProductResponseDto;
import com.example.project_employee.entity.CustomerEntity;
import com.example.project_employee.entity.OrderEntity;
import com.example.project_employee.entity.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderEntity toEntity(OrderRequestDto dto);


    @Mapping(target = "customerInfo", source = "customer",
            qualifiedByName = "mapCustomerInfo")
    @Mapping(target = "productInfo", source = "products",
            qualifiedByName = "mapProductInfoList")
    OrderResponseDto  toResponseDto(OrderEntity entity);


    @Named("mapCustomerInfo")
    default OrderResponseDto.CustomerInfo mapCustomerInfo(CustomerEntity entity) {
        if (entity == null) {
            return null;
        }
        OrderResponseDto.CustomerInfo info = new OrderResponseDto.CustomerInfo();
        info.setFirstName(entity.getFirstName());
        info.setLastName(entity.getLastName());
        info.setEmail(entity.getEmail());
        info.setPhoneNumber(entity.getPhoneNumber());
        return info;
    }

    @Named("mapProductInfo")
    default OrderResponseDto.ProductInfo mapProductInfo(ProductEntity entity) {
        if (entity == null) {
            return null;
        }
        OrderResponseDto.ProductInfo info = new OrderResponseDto.ProductInfo();
        info.setId(entity.getId());
        info.setName(entity.getName());
        info.setDescription(entity.getDescription());
        info.setPrice(entity.getPrice());
        return info;
    }

    @Named("mapProductInfoList")
    default List<OrderResponseDto.ProductInfo> mapProductInfoList(List<ProductEntity> entities) {
        if (entities == null) {
            return List.of();
        }
        return entities.stream()
                .map(this::mapProductInfo)
                .toList();
    }

    ProductResponseDto toProductResponseDto(ProductEntity entity);

    default List<ProductResponseDto> toProductResponseDtoList(List<ProductEntity> entities) {
        if (entities == null) {
            return List.of();
        }
        return entities.stream()
                .map(this::toProductResponseDto)
                .toList();
    }
}

