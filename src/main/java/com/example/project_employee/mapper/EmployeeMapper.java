package com.example.project_employee.mapper;

import com.example.project_employee.dto.EmployeeRequestDto;
import com.example.project_employee.dto.EmployeeResponseDto;
import com.example.project_employee.entity.EmployeeEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EmployeeMapper {

    EmployeeResponseDto toResponseDto(EmployeeEntity employeeEntity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    EmployeeEntity toEntity(EmployeeRequestDto requestDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "hireDate", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(@MappingTarget EmployeeEntity employeeEntity, EmployeeRequestDto requestDto);
}