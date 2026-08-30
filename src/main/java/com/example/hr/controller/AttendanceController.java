package com.example.hr.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.hr.dto.AttendanceDto;
import com.example.hr.service.AttendanceService;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {
    @Autowired
    private AttendanceService attendanceService;

    @PostMapping("/check-in/{employeeId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<AttendanceDto> checkIn(@PathVariable Long employeeId) {
        return ResponseEntity.ok(attendanceService.checkIn(employeeId));
    }

    @PostMapping("/check-out/{employeeId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<AttendanceDto> checkOut(@PathVariable Long employeeId) {
        return ResponseEntity.ok(attendanceService.checkOut(employeeId));
    }

    @GetMapping("/employee/{employeeId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR_MANAGER', 'MANAGER', 'EMPLOYEE')")
    public ResponseEntity<List<AttendanceDto>> getEmployeeAttendance(@PathVariable Long employeeId) {
        return ResponseEntity.ok(attendanceService.getEmployeeAttendance(employeeId));
    }

    @GetMapping("/daily/{workDate}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR_MANAGER', 'MANAGER')")
    public ResponseEntity<List<AttendanceDto>> getDailyAttendance(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate workDate) {
        return ResponseEntity.ok(attendanceService.getDailyAttendance(workDate));
    }

    @GetMapping("/report")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR_MANAGER', 'MANAGER')")
    public ResponseEntity<List<AttendanceDto>> getAttendanceReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(attendanceService.getAttendanceReport(startDate, endDate));
    }
}
