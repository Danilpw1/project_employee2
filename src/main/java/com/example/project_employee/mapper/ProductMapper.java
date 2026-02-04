package com.example.project_employee.mapper;

import com.example.project_employee.dto.ProductRequestDto;
import com.example.project_employee.dto.ProductResponseDto;
import com.example.project_employee.entity.ProductEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductEntity toEntity(ProductRequestDto dto);

    //    @Mapping(target = "info", source = ".",
//            qualifiedByName = "getProductInfo")
    ProductResponseDto toResponseDto(ProductEntity entity);

//    @Named("getProductInfo")
//    default String getProductInfo(ProductEntity entity) {
//        if (entity == null) {
//            return null;
//        }
//        return "Product name: " + entity.getName() +
//                " | Description: " + entity.getDescription() +
//                " | Price: " + String.format("%.2f RUB", entity.getPrice()
//        );
//    }
}
