package com.example.hr.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.hr.model.Attendance;
import com.example.hr.model.DashboardResponse;
import com.example.hr.model.Employee;
import com.example.hr.model.LeaveRequest;
import com.example.hr.service.HRService;

import jakarta.validation.Valid;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/legacy")
public class HRController {
    private final HRService service;

    public HRController(HRService service) {
        this.service = service;
    }

    @GetMapping("/dashboard")
    public ResponseEntity<DashboardResponse> dashboard() {
        return ResponseEntity.ok(service.dashboard());
    }

    @PostMapping("/employees")
    public ResponseEntity<Employee> createEmployee(@Valid @RequestBody Employee employee) {
        return ResponseEntity.ok(service.createEmployee(employee));
    }

    @PostMapping("/leaves")
    public ResponseEntity<LeaveRequest> createLeave(@Valid @RequestBody LeaveRequest leaveRequest) {
        return ResponseEntity.ok(service.createLeave(leaveRequest));
    }

    @PutMapping("/leaves/{id}/approve")
    public ResponseEntity<LeaveRequest> approveLeave(@PathVariable int id) {
        return ResponseEntity.ok(service.approveLeave(id));
    }

    @PostMapping("/attendance/check-in")
    public ResponseEntity<Attendance> checkIn(@RequestBody Attendance attendance) {
        return ResponseEntity.ok(service.checkIn(attendance.employee()));
    }
}
