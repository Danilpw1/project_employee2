package com.example.project_employee.service;

import com.example.project_employee.dto.EmployeeRequestDto;
import com.example.project_employee.dto.EmployeeResponseDto;
import com.example.project_employee.entity.EmployeeEntity;
import com.example.project_employee.mapper.EmployeeMapper;
import com.example.project_employee.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    public EmployeeResponseDto createEmployee(EmployeeRequestDto requestDto) {
        log.info("Создание нового сотрудника: {}", requestDto.getEmail());

        if (employeeRepository.existsByEmail(requestDto.getEmail())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Email '" + requestDto.getEmail() + "' уже используется"
            );
        }

        if (employeeRepository.existsByPhone(requestDto.getPhone())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Телефон '" + requestDto.getPhone() + "' уже используется"
            );
        }

        EmployeeEntity employeeEntity = employeeMapper.toEntity(requestDto);
        EmployeeEntity saved = employeeRepository.save(employeeEntity);
        log.info("Создан сотрудник с ID: {}", saved.getId());

        return employeeMapper.toResponseDto(saved);
    }

    @Transactional(readOnly = true)
    public EmployeeResponseDto getEmployeeById(Long id) {
        log.info("Получение сотрудника по ID: {}", id);

        EmployeeEntity employeeEntity = employeeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Сотрудник с ID " + id + " не найден"
                ));

        return employeeMapper.toResponseDto(employeeEntity);
    }

    public EmployeeResponseDto updateEmployee(Long id, EmployeeRequestDto requestDto) {
        log.info("Обновление сотрудника с ID: {}", id);

        EmployeeEntity employeeEntity = employeeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Сотрудник с ID " + id + " не найден"
                ));

        if (!employeeEntity.getEmail().equals(requestDto.getEmail()) &&
                employeeRepository.existsByEmailAndIdNot(requestDto.getEmail(), id)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Email '" + requestDto.getEmail() + "' уже используется другим сотрудником"
            );
        }

        if (!employeeEntity.getPhone().equals(requestDto.getPhone()) &&
                employeeRepository.existsByPhoneAndIdNot(requestDto.getPhone(), id)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Телефон '" + requestDto.getPhone() + "' уже используется другим сотрудником"
            );
        }

        employeeMapper.updateEntity(employeeEntity, requestDto);
        EmployeeEntity updated = employeeRepository.save(employeeEntity);
        log.info("Обновлен сотрудник с ID: {}", updated.getId());

        return employeeMapper.toResponseDto(updated);
    }

    public void deleteEmployee(Long id) {
        log.info("Удаление сотрудника с ID: {}", id);

        if (!employeeRepository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Сотрудник с ID " + id + " не найден"
            );
        }

        employeeRepository.deleteById(id);
        log.info("Удален сотрудник с ID: {}", id);
    }

    @Transactional(readOnly = true)
    public Page<EmployeeResponseDto> getAllEmployees(Pageable pageable) {
        log.info("Получение всех сотрудников с пагинацией: page={}, size={}",
                pageable.getPageNumber(), pageable.getPageSize());

        return employeeRepository.findAll(pageable)
                .map(employeeMapper::toResponseDto);
    }

    @Transactional(readOnly = true)
    public List<EmployeeResponseDto> getAllEmployees() {
        log.info("Получение всех сотрудников без пагинации");

        return employeeRepository.findAll().stream()
                .map(employeeMapper::toResponseDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public Page<EmployeeResponseDto> getEmployeesWithFilters(
            String firstName,
            String lastName,
            String department,
            String position,
            BigDecimal minSalary,
            BigDecimal maxSalary,
            LocalDate hireDateFrom,
            LocalDate hireDateTo,
            Pageable pageable) {

        log.info("Фильтрация сотрудников с параметрами: firstName={}, lastName={}, department={}, position={}",
                firstName, lastName, department, position);

        Page<EmployeeEntity> employeeEntities = employeeRepository.findByAllFilters(
                firstName,
                lastName,
                department,
                position,
                minSalary,
                maxSalary,
                hireDateFrom,
                hireDateTo,
                pageable
        );

        return employeeEntities.map(employeeMapper::toResponseDto);
    }

    @Transactional(readOnly = true)
    public List<EmployeeResponseDto> getEmployeesByDepartment(String department) {
        log.info("Поиск сотрудников по отделу: {}", department);

        if (!StringUtils.hasText(department)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Отдел не указан"
            );
        }

        return employeeRepository.findByDepartment(department).stream()
                .map(employeeMapper::toResponseDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<EmployeeResponseDto> getEmployeesByPosition(String position) {
        log.info("Поиск сотрудников по должности: {}", position);

        if (!StringUtils.hasText(position)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Должность не указана"
            );
        }

        return employeeRepository.findByPosition(position).stream()
                .map(employeeMapper::toResponseDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<EmployeeResponseDto> getEmployeesBySalaryRange(BigDecimal minSalary, BigDecimal maxSalary) {
        log.info("Поиск сотрудников по диапазону зарплат: {} - {}", minSalary, maxSalary);

        if (minSalary == null || maxSalary == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Минимальная и максимальная зарплата должны быть указаны"
            );
        }

        if (minSalary.compareTo(maxSalary) > 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Минимальная зарплата не может быть больше максимальной"
            );
        }

        return employeeRepository.findBySalaryBetween(minSalary, maxSalary).stream()
                .map(employeeMapper::toResponseDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<EmployeeResponseDto> getEmployeesByHireDateRange(LocalDate from, LocalDate to) {
        log.info("Поиск сотрудников по дате приема: {} - {}", from, to);

        if (from == null || to == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Даты начала и конца диапазона должны быть указаны"
            );
        }

        if (from.isAfter(to)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Дата начала не может быть позже даты окончания"
            );
        }

        return employeeRepository.findByHireDateBetween(from, to).stream()
                .map(employeeMapper::toResponseDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<EmployeeResponseDto> searchEmployeesByName(String name) {
        log.info("Поиск сотрудников по имени: {}", name);

        if (!StringUtils.hasText(name)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Имя для поиска не указано"
            );
        }

        return employeeRepository.findByNameContaining(name).stream()
                .map(employeeMapper::toResponseDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return employeeRepository.existsByEmail(email);
    }

    @Transactional(readOnly = true)
    public EmployeeResponseDto getEmployeeByEmail(String email) {
        log.info("Поиск сотрудника по email: {}", email);

        EmployeeEntity employeeEntity = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Сотрудник с email '" + email + "' не найден"
                ));

        return employeeMapper.toResponseDto(employeeEntity);
    }
}