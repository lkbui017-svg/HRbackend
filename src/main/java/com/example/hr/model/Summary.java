package com.example.hr.model;

public record Summary(
        int employees,
        long activeEmployees,
        long pendingLeaves,
        int todayCheckins,
        int openJobs,
        int candidates
) {
}
