package com.example.hr.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.hr.dto.AttendanceDto;
import com.example.hr.entity.Attendance;
import com.example.hr.entity.Employee;
import com.example.hr.exception.ResourceNotFoundException;
import com.example.hr.repository.AttendanceRepository;
import com.example.hr.repository.EmployeeRepository;

@Service
public class AttendanceService {
    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    public AttendanceDto checkIn(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found: " + employeeId));

        LocalDate today = LocalDate.now();

        Attendance attendance = attendanceRepository.findByEmployeeIdAndWorkDate(employeeId, today)
                .orElseGet(() -> {
                    Attendance newAttendance = new Attendance(employee, today, LocalTime.now(), "QR");
                    return attendanceRepository.save(newAttendance);
                });

        return toResponse(attendance);
    }

    public AttendanceDto checkOut(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found: " + employeeId));

        LocalDate today = LocalDate.now();
        Attendance attendance = attendanceRepository.findByEmployeeIdAndWorkDate(employeeId, today)
                .orElseThrow(() -> new ResourceNotFoundException("No check-in record for today"));

        attendance.setCheckOutTime(LocalTime.now());
        Attendance updated = attendanceRepository.save(attendance);

        return toResponse(updated);
    }

    public List<AttendanceDto> getEmployeeAttendance(Long employeeId) {
        return attendanceRepository.findByEmployeeId(employeeId).stream()
                .map(this::toResponse)
                .toList();
    }

    public List<AttendanceDto> getDailyAttendance(LocalDate workDate) {
        return attendanceRepository.findByWorkDate(workDate).stream()
                .map(this::toResponse)
                .toList();
    }

    public List<AttendanceDto> getAttendanceReport(LocalDate startDate, LocalDate endDate) {
        return attendanceRepository.findByWorkDateBetween(startDate, endDate).stream()
                .map(this::toResponse)
                .toList();
    }

    private AttendanceDto toResponse(Attendance attendance) {
        return new AttendanceDto(
                attendance.getEmployee().getId(),
                attendance.getEmployee().getFullName(),
                attendance.getWorkDate(),
                attendance.getCheckInTime(),
                attendance.getCheckOutTime(),
                attendance.getMethod()
        );
    }
}
