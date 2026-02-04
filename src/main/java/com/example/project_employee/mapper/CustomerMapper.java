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

    //    @Mapping(target = "info", source = ".",
//             qualifiedByName = "getCustomerInfo")
    @Mapping(target = "orders", source = "orders")
    CustomerResponseDto toResponseDto(CustomerEntity entity);

//    @Named("getCustomerInfo")
//    default String getCustomerInfo(CustomerEntity entity) {
//        return entity.getFirstName() + " " + entity.getLastName() +
//                " ,email: " + entity.getEmail() +
//                " ,phone number: " + entity.getPhoneNumber() +
//                " ,orders: " +  (entity.getOrders() == null ? 0 : entity.getOrders().size());
//    }

    @Mapping(source = "orderStatus", target = "status")
    CustomerResponseDto.OrderInfo mapOrderInfo(OrderEntity orderEntity);
}
