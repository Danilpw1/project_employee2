package com.example.project_employee.repository;

import com.example.project_employee.entity.EmployeeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Long> {

    List<EmployeeEntity> findByDepartment(String department);

    List<EmployeeEntity> findByPosition(String position);

    List<EmployeeEntity> findByDepartmentAndPosition(String department, String position);

    List<EmployeeEntity> findBySalaryBetween(BigDecimal minSalary, BigDecimal maxSalary);

    List<EmployeeEntity> findByHireDateBetween(LocalDate startDate, LocalDate endDate);

    Optional<EmployeeEntity> findByEmail(String email);

    Optional<EmployeeEntity> findByPhone(String phone);

    Page<EmployeeEntity> findByDepartment(String department, Pageable pageable);

    Page<EmployeeEntity> findByPosition(String position, Pageable pageable);

    List<EmployeeEntity> findByFirstNameContainingIgnoreCase(String firstName);

    List<EmployeeEntity> findByLastNameContainingIgnoreCase(String lastName);

    @Query("SELECT e FROM EmployeeEntity e WHERE " +
            "LOWER(e.firstName) LIKE LOWER(CONCAT('%', :name, '%')) OR " +
            "LOWER(e.lastName) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<EmployeeEntity> findByNameContaining(@Param("name") String name);

    @Query("SELECT e FROM EmployeeEntity e WHERE " +
            "(:firstName IS NULL OR LOWER(e.firstName) LIKE LOWER(CONCAT('%', :firstName, '%'))) AND " +
            "(:lastName IS NULL OR LOWER(e.lastName) LIKE LOWER(CONCAT('%', :lastName, '%'))) AND " +
            "(:department IS NULL OR e.department = :department) AND " +
            "(:position IS NULL OR e.position = :position) AND " +
            "(:minSalary IS NULL OR e.salary >= :minSalary) AND " +
            "(:maxSalary IS NULL OR e.salary <= :maxSalary) AND " +
            "(:hireDateFrom IS NULL OR e.hireDate >= :hireDateFrom) AND " +
            "(:hireDateTo IS NULL OR e.hireDate <= :hireDateTo)")
    Page<EmployeeEntity> findByAllFilters(
            @Param("firstName") String firstName,
            @Param("lastName") String lastName,
            @Param("department") String department,
            @Param("position") String position,
            @Param("minSalary") BigDecimal minSalary,
            @Param("maxSalary") BigDecimal maxSalary,
            @Param("hireDateFrom") LocalDate hireDateFrom,
            @Param("hireDateTo") LocalDate hireDateTo,
            Pageable pageable);

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END " +
            "FROM EmployeeEntity e WHERE e.email = :email AND e.id != :id")
    boolean existsByEmailAndIdNot(@Param("email") String email, @Param("id") Long id);

    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END " +
            "FROM EmployeeEntity e WHERE e.phone = :phone AND e.id != :id")
    boolean existsByPhoneAndIdNot(@Param("phone") String phone, @Param("id") Long id);
}