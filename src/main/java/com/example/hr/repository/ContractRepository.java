package com.example.hr.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.hr.entity.Contract;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Long> {
    Optional<Contract> findByEmployeeId(Long employeeId);

    List<Contract> findByContractType(String contractType);

    List<Contract> findByEmployeeIdOrderByStartDateDesc(Long employeeId);
}
