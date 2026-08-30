package com.example.hr.model;

import jakarta.validation.constraints.NotBlank;

public record LeaveRequest(
        int id,
        @NotBlank String employee,
        @NotBlank String fromDate,
        @NotBlank String toDate,
        @NotBlank String reason,
        String status
) {
}
