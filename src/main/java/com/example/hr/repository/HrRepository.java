package com.example.hr.repository;

import java.util.List;

import com.example.hr.model.Attendance;
import com.example.hr.model.Candidate;
import com.example.hr.model.Contract;
import com.example.hr.model.Employee;
import com.example.hr.model.JobPost;
import com.example.hr.model.LeaveRequest;

public interface HrRepository {
    List<Employee> getEmployees();

    List<Contract> getContracts();

    List<LeaveRequest> getLeaves();

    List<Attendance> getAttendances();

    List<JobPost> getJobs();

    List<Candidate> getCandidates();

    Employee saveEmployee(Employee employee);

    LeaveRequest saveLeave(LeaveRequest leaveRequest);

    LeaveRequest updateLeave(LeaveRequest leaveRequest);

    Attendance saveAttendance(Attendance attendance);
}
