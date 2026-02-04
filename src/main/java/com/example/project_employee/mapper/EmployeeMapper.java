package com.example.project_employee.mapper;

import com.example.project_employee.dto.EmployeeRequestDto;
import com.example.project_employee.dto.EmployeeResponseDto;
import com.example.project_employee.entity.EmployeeEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    EmployeeEntity toEntity(EmployeeRequestDto dto);

    //    @Mapping(target = "info", source = ".",
//            qualifiedByName = "getEmployeeInfo")
    EmployeeResponseDto toResponseDto(EmployeeEntity entity);

//    @Named("getEmployeeInfo")
//    default String getEmployeeInfo(EmployeeEntity entity) {
//        return entity.getFirstName() + " " + entity.getLastName() +
//                " ,email: " + entity.getEmail() +
//                " ,password: " + entity.getPassword() +
//                " ,role: " + entity.getRole();
//    }
}
