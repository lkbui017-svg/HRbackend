package com.example.hr.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.hr.dto.LeaveRequestDto;
import com.example.hr.dto.LeaveResponseDto;
import com.example.hr.service.LeaveService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/leaves")
public class LeaveController {
    @Autowired
    private LeaveService leaveService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'HR_MANAGER', 'MANAGER')")
    public ResponseEntity<List<LeaveResponseDto>> getAllLeaves() {
        return ResponseEntity.ok(leaveService.getAllLeaves());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR_MANAGER', 'MANAGER', 'EMPLOYEE')")
    public ResponseEntity<LeaveResponseDto> getLeaveById(@PathVariable Long id) {
        return ResponseEntity.ok(leaveService.getLeaveById(id));
    }

    @PostMapping("/employee/{employeeId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR_MANAGER', 'EMPLOYEE')")
    public ResponseEntity<LeaveResponseDto> createLeave(@PathVariable Long employeeId, @Valid @RequestBody LeaveRequestDto request) {
        return new ResponseEntity<>(leaveService.createLeave(employeeId, request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}/approve")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR_MANAGER', 'MANAGER')")
    public ResponseEntity<LeaveResponseDto> approveLeave(@PathVariable Long id, @RequestParam String approverName) {
        return ResponseEntity.ok(leaveService.approveLeave(id, approverName));
    }

    @PutMapping("/{id}/reject")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR_MANAGER', 'MANAGER')")
    public ResponseEntity<LeaveResponseDto> rejectLeave(@PathVariable Long id, @RequestParam String approverName) {
        return ResponseEntity.ok(leaveService.rejectLeave(id, approverName));
    }

    @GetMapping("/employee/{employeeId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR_MANAGER', 'MANAGER', 'EMPLOYEE')")
    public ResponseEntity<List<LeaveResponseDto>> getLeavesByEmployee(@PathVariable Long employeeId) {
        return ResponseEntity.ok(leaveService.getLeavesByEmployee(employeeId));
    }

    @GetMapping("/status/pending")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR_MANAGER', 'MANAGER')")
    public ResponseEntity<List<LeaveResponseDto>> getPendingLeaves() {
        return ResponseEntity.ok(leaveService.getPendingLeaves());
    }
}
