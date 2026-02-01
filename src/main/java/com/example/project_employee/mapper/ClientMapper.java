package com.example.project_employee.mapper;

import com.example.project_employee.dto.ClientRequestDto;
import com.example.project_employee.dto.ClientResponseDto;
import com.example.project_employee.dto.ClientSimpleDto;
import com.example.project_employee.entity.ClientEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ClientMapper {

    // ТОЛЬКО ОДИН метод toResponseDto
    ClientResponseDto toResponseDto(ClientEntity clientEntity);

    ClientSimpleDto toSimpleDto(ClientEntity clientEntity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "orders", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    ClientEntity toEntity(ClientRequestDto requestDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "orders", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(@MappingTarget ClientEntity clientEntity, ClientRequestDto requestDto);
}