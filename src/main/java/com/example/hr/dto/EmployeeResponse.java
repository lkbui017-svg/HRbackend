package com.example.hr.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record EmployeeResponse(
        Long id,
        String fullName,
        String department,
        String position,
        String email,
        String phone,
        String status,
        LocalDate hireDate,
        String contractType,
        BigDecimal salary
) {
}
