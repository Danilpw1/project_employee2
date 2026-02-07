package com.example.project_employee.mapper;

import com.example.project_employee.dto.ProductRequestDto;
import com.example.project_employee.dto.ProductResponseDto;
import com.example.project_employee.entity.ProductEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductEntity toEntity(ProductRequestDto dto);

    ProductResponseDto toResponseDto(ProductEntity entity);

}
