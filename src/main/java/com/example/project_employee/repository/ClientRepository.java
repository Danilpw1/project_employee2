package com.example.project_employee.repository;

import com.example.project_employee.entity.ClientEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<ClientEntity, Long> {

    Optional<ClientEntity> findByEmail(String email);

    Optional<ClientEntity> findByPhone(String phone);

    List<ClientEntity> findByFirstNameContainingIgnoreCase(String firstName);

    List<ClientEntity> findByLastNameContainingIgnoreCase(String lastName);

    @Query("SELECT c FROM ClientEntity c WHERE " +
            "(:firstName IS NULL OR LOWER(c.firstName) LIKE LOWER(CONCAT('%', :firstName, '%'))) AND " +
            "(:lastName IS NULL OR LOWER(c.lastName) LIKE LOWER(CONCAT('%', :lastName, '%'))) AND " +
            "(:email IS NULL OR LOWER(c.email) LIKE LOWER(CONCAT('%', :email, '%'))) AND " +
            "(:phone IS NULL OR LOWER(c.phone) LIKE LOWER(CONCAT('%', :phone, '%')))")
    Page<ClientEntity> findByAllFilters(
            @Param("firstName") String firstName,
            @Param("lastName") String lastName,
            @Param("email") String email,
            @Param("phone") String phone,
            Pageable pageable);

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END " +
            "FROM ClientEntity c WHERE c.email = :email AND c.id != :id")
    boolean existsByEmailAndIdNot(@Param("email") String email, @Param("id") Long id);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END " +
            "FROM ClientEntity c WHERE c.phone = :phone AND c.id != :id")
    boolean existsByPhoneAndIdNot(@Param("phone") String phone, @Param("id") Long id);
}