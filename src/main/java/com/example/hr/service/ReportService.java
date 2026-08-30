package com.example.hr.service;

import com.example.hr.entity.EmployeeStatus;
import com.example.hr.entity.LeaveStatus;
import com.example.hr.repository.EmployeeRepository;
import com.example.hr.repository.LeaveRequestRepository;
import com.example.hr.repository.AttendanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
public class ReportService {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private LeaveRequestRepository leaveRequestRepository;

    @Autowired
    private AttendanceRepository attendanceRepository;

    public Map<String, Object> getDashboardSummary() {
        Map<String, Object> summary = new HashMap<>();

        // Employee statistics
        long totalEmployees = employeeRepository.count();
        long activeEmployees = employeeRepository.countByStatus(EmployeeStatus.ACTIVE);
        long newbies = employeeRepository.countByHireDateAfter(LocalDate.now().minusMonths(3));

        summary.put("totalEmployees", totalEmployees);
        summary.put("activeEmployees", activeEmployees);
        summary.put("newHiresLast3Months", newbies);

        // Leave statistics
        long pendingLeaves = leaveRequestRepository.countByStatus(LeaveStatus.PENDING);
        long approvedLeaves = leaveRequestRepository.countByStatus(LeaveStatus.APPROVED);
        long totalLeaves = leaveRequestRepository.count();

        summary.put("pendingLeaveRequests", pendingLeaves);
        summary.put("approvedLeaveRequests", approvedLeaves);
        summary.put("totalLeaveRequests", totalLeaves);

        // Attendance statistics
        long todayAttendance = attendanceRepository.countByWorkDate(LocalDate.now());
        double attendanceRate = totalEmployees > 0 ? (todayAttendance / (double) totalEmployees) * 100 : 0;

        summary.put("todayAttendance", todayAttendance);
        summary.put("attendanceRate", String.format("%.2f%%", attendanceRate));

        return summary;
    }

    public Map<String, Long> getEmployeesByDepartment() {
        Map<String, Long> departments = new HashMap<>();

        departmentCounts().forEach((dept, count) -> {
            departments.put(dept, count);
        });

        return departments;
    }

    public Map<String, Object> getMonthlyAttendanceReport(int year, int month) {
        Map<String, Object> report = new HashMap<>();

        LocalDate monthStart = LocalDate.of(year, month, 1);
        LocalDate monthEnd = monthStart.plusMonths(1).minusDays(1);

        long recordsCount = attendanceRepository.countByWorkDateBetween(monthStart, monthEnd);
        long totalEmployees = employeeRepository.countByStatus(EmployeeStatus.ACTIVE);
        double avgAttendanceRate = totalEmployees > 0 ? (recordsCount / (double) (totalEmployees * monthEnd.getDayOfMonth())) * 100 : 0;

        report.put("year", year);
        report.put("month", month);
        report.put("totalRecords", recordsCount);
        report.put("totalWorkDays", monthEnd.getDayOfMonth());
        report.put("avgAttendanceRate", String.format("%.2f%%", avgAttendanceRate));

        return report;
    }

    public Map<String, Object> getLeaveAnalytics() {
        Map<String, Object> analytics = new HashMap<>();

        long annual = leaveRequestRepository.countByLeaveType(com.example.hr.entity.LeaveType.ANNUAL);
        long sick = leaveRequestRepository.countByLeaveType(com.example.hr.entity.LeaveType.SICK);
        long maternity = leaveRequestRepository.countByLeaveType(com.example.hr.entity.LeaveType.MATERNITY);
        long personal = leaveRequestRepository.countByLeaveType(com.example.hr.entity.LeaveType.PERSONAL);
        long other = leaveRequestRepository.countByLeaveType(com.example.hr.entity.LeaveType.OTHER);

        analytics.put("annual", annual);
        analytics.put("sick", sick);
        analytics.put("maternity", maternity);
        analytics.put("personal", personal);
        analytics.put("other", other);

        return analytics;
    }

    private Map<String, Long> departmentCounts() {
        Map<String, Long> deptCounts = new HashMap<>();
        employeeRepository.findAll().forEach(emp -> {
            deptCounts.put(emp.getDepartment(), deptCounts.getOrDefault(emp.getDepartment(), 0L) + 1);
        });
        return deptCounts;
    }
}
