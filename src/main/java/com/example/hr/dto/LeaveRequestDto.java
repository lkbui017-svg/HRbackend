package com.example.hr.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LeaveRequestDto(
        @NotBlank String leaveType,
        @NotNull LocalDate fromDate,
        @NotNull LocalDate toDate,
        @NotBlank String reason
) {
}
