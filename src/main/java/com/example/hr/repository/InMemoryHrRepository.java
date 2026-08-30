package com.example.hr.repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.example.hr.model.Attendance;
import com.example.hr.model.Candidate;
import com.example.hr.model.Contract;
import com.example.hr.model.Employee;
import com.example.hr.model.JobPost;
import com.example.hr.model.LeaveRequest;

@Repository
public class InMemoryHrRepository implements HrRepository {
    private final List<Employee> employees = new ArrayList<>();
    private final List<Contract> contracts = new ArrayList<>();
    private final List<LeaveRequest> leaves = new ArrayList<>();
    private final List<Attendance> attendances = new ArrayList<>();
    private final List<JobPost> jobs = new ArrayList<>();
    private final List<Candidate> candidates = new ArrayList<>();

    public InMemoryHrRepository() {
        seed();
    }

    @Override
    public List<Employee> getEmployees() {
        return employees;
    }

    @Override
    public List<Contract> getContracts() {
        return contracts;
    }

    @Override
    public List<LeaveRequest> getLeaves() {
        return leaves;
    }

    @Override
    public List<Attendance> getAttendances() {
        return attendances;
    }

    @Override
    public List<JobPost> getJobs() {
        return jobs;
    }

    @Override
    public List<Candidate> getCandidates() {
        return candidates;
    }

    @Override
    public Employee saveEmployee(Employee employee) {
        employees.add(employee);
        return employee;
    }

    @Override
    public LeaveRequest saveLeave(LeaveRequest leaveRequest) {
        leaves.add(leaveRequest);
        return leaveRequest;
    }

    @Override
    public LeaveRequest updateLeave(LeaveRequest leaveRequest) {
        for (int index = 0; index < leaves.size(); index++) {
            if (leaves.get(index).id() == leaveRequest.id()) {
                leaves.set(index, leaveRequest);
                return leaveRequest;
            }
        }
        throw new IllegalArgumentException("Leave request not found: " + leaveRequest.id());
    }

    @Override
    public Attendance saveAttendance(Attendance attendance) {
        attendances.add(attendance);
        return attendance;
    }

    private void seed() {
        employees.add(new Employee(1, "Nguyen Minh Anh", "Nhan su", "HR Manager", "anh.nguyen@company.vn", "Dang lam viec"));
        employees.add(new Employee(2, "Tran Quoc Bao", "Ky thuat", "Frontend Developer", "bao.tran@company.vn", "Dang lam viec"));
        employees.add(new Employee(3, "Le Thu Ha", "Kinh doanh", "Sales Lead", "ha.le@company.vn", "Dang lam viec"));
        employees.add(new Employee(4, "Pham Gia Huy", "Tai chinh", "Accountant", "huy.pham@company.vn", "Thu viec"));

        contracts.add(new Contract("Nguyen Minh Anh", "Khong xac dinh thoi han", "2021-03-01", "-", "32,000,000 VND"));
        contracts.add(new Contract("Tran Quoc Bao", "12 thang", "2026-01-10", "2027-01-09", "24,000,000 VND"));
        contracts.add(new Contract("Pham Gia Huy", "Thu viec", "2026-08-01", "2026-09-30", "14,000,000 VND"));

        leaves.add(new LeaveRequest(1, "Le Thu Ha", "2026-08-26", "2026-08-27", "Viec gia dinh", "Cho duyet"));
        leaves.add(new LeaveRequest(2, "Tran Quoc Bao", "2026-08-20", "2026-08-20", "Kham suc khoe", "Da duyet"));

        attendances.add(new Attendance("Nguyen Minh Anh", LocalDate.now().toString(), "08:03:12", "QR"));
        attendances.add(new Attendance("Le Thu Ha", LocalDate.now().toString(), "08:14:33", "QR"));

        jobs.add(new JobPost("Java Backend Developer", "Ky thuat", "2026-09-15", "Dang tuyen"));
        jobs.add(new JobPost("HR Executive", "Nhan su", "2026-09-05", "Dang tuyen"));

        candidates.add(new Candidate("Doan Thanh Tam", "Java Backend Developer", "tam-doan-cv.pdf", "Phong van vong 1"));
        candidates.add(new Candidate("Vu My Linh", "HR Executive", "linh-vu-cv.pdf", "Moi nop CV"));
    }
}
