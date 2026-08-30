package com.example.hr.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.hr.exception.ResourceNotFoundException;
import com.example.hr.model.Attendance;
import com.example.hr.model.Candidate;
import com.example.hr.model.Contract;
import com.example.hr.model.DashboardResponse;
import com.example.hr.model.Employee;
import com.example.hr.model.JobPost;
import com.example.hr.model.LeaveRequest;
import com.example.hr.model.Summary;
import com.example.hr.repository.HrRepository;

@Service
public class HRService {
    private final HrRepository repository;

    public HRService(HrRepository repository) {
        this.repository = repository;
    }

    public DashboardResponse dashboard() {
        List<Employee> employees = repository.getEmployees();
        List<Contract> contracts = repository.getContracts();
        List<LeaveRequest> leaves = repository.getLeaves();
        List<Attendance> attendances = repository.getAttendances();
        List<JobPost> jobs = repository.getJobs();
        List<Candidate> candidates = repository.getCandidates();

        long activeEmployees = employees.stream()
                .filter(employee -> "Dang lam viec".equals(employee.status()))
                .count();
        long pendingLeaves = leaves.stream()
                .filter(leave -> "Cho duyet".equals(leave.status()))
                .count();

        Summary summary = new Summary(
                employees.size(),
                activeEmployees,
                pendingLeaves,
                attendances.size(),
                jobs.size(),
                candidates.size()
        );

        return new DashboardResponse(summary, employees, contracts, leaves, attendances, jobs, candidates);
    }

    public Employee createEmployee(Employee request) {
        int id = repository.getEmployees().stream().mapToInt(Employee::id).max().orElse(0) + 1;
        Employee employee = new Employee(
                id,
                request.name(),
                request.department(),
                request.role(),
                request.email(),
                "Dang lam viec"
        );
        return repository.saveEmployee(employee);
    }

    public LeaveRequest createLeave(LeaveRequest request) {
        int id = repository.getLeaves().stream().mapToInt(LeaveRequest::id).max().orElse(0) + 1;
        LeaveRequest leave = new LeaveRequest(
                id,
                request.employee(),
                request.fromDate(),
                request.toDate(),
                request.reason(),
                "Cho duyet"
        );
        return repository.saveLeave(leave);
    }

    public LeaveRequest approveLeave(int id) {
        for (LeaveRequest leave : repository.getLeaves()) {
            if (leave.id() == id) {
                LeaveRequest approved = new LeaveRequest(
                        leave.id(),
                        leave.employee(),
                        leave.fromDate(),
                        leave.toDate(),
                        leave.reason(),
                        "Da duyet"
                );
                return repository.updateLeave(approved);
            }
        }
        throw new ResourceNotFoundException("Leave request not found: " + id);
    }

    public Attendance checkIn(String employeeName) {
        String employee = employeeName == null || employeeName.isBlank() ? "Nhan vien demo" : employeeName;
        Attendance attendance = new Attendance(
                employee,
                LocalDate.now().toString(),
                LocalTime.now().withNano(0).toString(),
                "QR"
        );
        return repository.saveAttendance(attendance);
    }
}
