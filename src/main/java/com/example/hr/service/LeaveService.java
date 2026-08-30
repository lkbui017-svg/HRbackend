package com.example.hr.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.hr.dto.LeaveRequestDto;
import com.example.hr.dto.LeaveResponseDto;
import com.example.hr.entity.Employee;
import com.example.hr.entity.LeaveRequest;
import com.example.hr.entity.LeaveStatus;
import com.example.hr.entity.LeaveType;
import com.example.hr.exception.ResourceNotFoundException;
import com.example.hr.repository.EmployeeRepository;
import com.example.hr.repository.LeaveRequestRepository;

@Service
public class LeaveService {
    @Autowired
    private LeaveRequestRepository leaveRequestRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    public List<LeaveResponseDto> getAllLeaves() {
        return leaveRequestRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public LeaveResponseDto getLeaveById(Long id) {
        LeaveRequest leave = leaveRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Leave request not found: " + id));
        return toResponse(leave);
    }

    public LeaveResponseDto createLeave(Long employeeId, LeaveRequestDto request) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found: " + employeeId));

        LeaveRequest leave = new LeaveRequest(
                employee,
                LeaveType.valueOf(request.leaveType()),
                request.fromDate(),
                request.toDate(),
                request.reason()
        );

        LeaveRequest saved = leaveRequestRepository.save(leave);
        return toResponse(saved);
    }

    public LeaveResponseDto approveLeave(Long id, String approverName) {
        LeaveRequest leave = leaveRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Leave request not found: " + id));

        leave.setStatus(LeaveStatus.APPROVED);
        leave.setApproverName(approverName);

        LeaveRequest updated = leaveRequestRepository.save(leave);
        return toResponse(updated);
    }

    public LeaveResponseDto rejectLeave(Long id, String approverName) {
        LeaveRequest leave = leaveRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Leave request not found: " + id));

        leave.setStatus(LeaveStatus.REJECTED);
        leave.setApproverName(approverName);

        LeaveRequest updated = leaveRequestRepository.save(leave);
        return toResponse(updated);
    }

    public List<LeaveResponseDto> getLeavesByEmployee(Long employeeId) {
        return leaveRequestRepository.findByEmployeeId(employeeId).stream()
                .map(this::toResponse)
                .toList();
    }

    public List<LeaveResponseDto> getPendingLeaves() {
        return leaveRequestRepository.findByStatus(LeaveStatus.PENDING).stream()
                .map(this::toResponse)
                .toList();
    }

    private LeaveResponseDto toResponse(LeaveRequest leave) {
        return new LeaveResponseDto(
                leave.getId(),
                leave.getEmployee().getId(),
                leave.getEmployee().getFullName(),
                leave.getLeaveType().toString(),
                leave.getFromDate(),
                leave.getToDate(),
                leave.getReason(),
                leave.getStatus().toString(),
                leave.getApproverName()
        );
    }
}
