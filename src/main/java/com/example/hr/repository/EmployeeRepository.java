package com.example.hr.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.hr.entity.Employee;
import com.example.hr.entity.EmployeeStatus;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByEmail(String email);

    List<Employee> findByDepartment(String department);

    List<Employee> findByStatus(EmployeeStatus status);

    boolean existsByEmail(String email);

    long countByStatus(EmployeeStatus status);

    long countByHireDateAfter(LocalDate date);

    List<Employee> findByHireDateAfter(LocalDate date);
}
