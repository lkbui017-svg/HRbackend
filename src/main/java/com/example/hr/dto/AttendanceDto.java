package com.example.hr.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record AttendanceDto(
        Long employeeId,
        String employeeName,
        LocalDate workDate,
        LocalTime checkInTime,
        LocalTime checkOutTime,
        String method
) {
}
