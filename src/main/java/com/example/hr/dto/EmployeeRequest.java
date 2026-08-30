package com.example.hr.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EmployeeRequest(
        @NotBlank String fullName,
        @NotBlank String department,
        @NotBlank String position,
        @Email String email,
        String phone,
        @NotNull LocalDate hireDate,
        @NotBlank String contractType,
        BigDecimal salary
) {
}
