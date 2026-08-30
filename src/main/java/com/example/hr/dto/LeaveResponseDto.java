package com.example.hr.dto;

import java.time.LocalDate;

public record LeaveResponseDto(
        Long id,
        Long employeeId,
        String employeeName,
        String leaveType,
        LocalDate fromDate,
        LocalDate toDate,
        String reason,
        String status,
        String approverName
) {
}
