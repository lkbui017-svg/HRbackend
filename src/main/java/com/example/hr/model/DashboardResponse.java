package com.example.hr.model;

import java.util.List;

public record DashboardResponse(
        Summary summary,
        List<Employee> employees,
        List<Contract> contracts,
        List<LeaveRequest> leaves,
        List<Attendance> attendance,
        List<JobPost> jobs,
        List<Candidate> candidates
) {
}
