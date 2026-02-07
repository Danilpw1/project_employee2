package com.example.project_employee.mapper;

import com.example.project_employee.dto.CustomerRequestDto;
import com.example.project_employee.dto.CustomerResponseDto;
import com.example.project_employee.entity.CustomerEntity;
import com.example.project_employee.entity.OrderEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {OrderMapper.class})
public interface CustomerMapper {

    CustomerEntity toEntity(CustomerRequestDto dto);

    @Mapping(target = "orders", source = "orders")
    CustomerResponseDto toResponseDto(CustomerEntity entity);

    @Mapping(source = "orderStatus", target = "status")
    CustomerResponseDto.OrderInfo mapOrderInfo(OrderEntity orderEntity);
}
