package com.example.project_employee.mapper;

import com.example.project_employee.dto.ProductRequestDto;
import com.example.project_employee.dto.ProductResponseDto;
import com.example.project_employee.entity.ProductEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductMapper {

    ProductResponseDto toResponseDto(ProductEntity productEntity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    ProductEntity toEntity(ProductRequestDto requestDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(@MappingTarget ProductEntity productEntity, ProductRequestDto requestDto);
}