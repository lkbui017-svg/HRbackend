package com.example.hr.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.hr.service.ReportService;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {
    @Autowired
    private ReportService reportService;

    @GetMapping("/summary")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR_MANAGER', 'MANAGER')")
    public ResponseEntity<Map<String, Object>> getDashboardSummary() {
        return ResponseEntity.ok(reportService.getDashboardSummary());
    }

    @GetMapping("/departments")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR_MANAGER')")
    public ResponseEntity<Map<String, Long>> getEmployeesByDepartment() {
        return ResponseEntity.ok(reportService.getEmployeesByDepartment());
    }

    @GetMapping("/attendance-report")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR_MANAGER', 'MANAGER')")
    public ResponseEntity<Map<String, Object>> getMonthlyAttendanceReport(
            @RequestParam int year,
            @RequestParam int month) {
        return ResponseEntity.ok(reportService.getMonthlyAttendanceReport(year, month));
    }

    @GetMapping("/leave-analytics")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR_MANAGER')")
    public ResponseEntity<Map<String, Object>> getLeaveAnalytics() {
        return ResponseEntity.ok(reportService.getLeaveAnalytics());
    }
}
