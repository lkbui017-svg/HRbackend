package com.example.hr.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.hr.entity.LeaveRequest;
import com.example.hr.entity.LeaveStatus;
import com.example.hr.entity.LeaveType;

@Repository
public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {
    List<LeaveRequest> findByEmployeeId(Long employeeId);

    List<LeaveRequest> findByStatus(LeaveStatus status);

    List<LeaveRequest> findByFromDateBetween(LocalDate startDate, LocalDate endDate);

    List<LeaveRequest> findByEmployeeIdAndFromDateBetween(Long employeeId, LocalDate startDate, LocalDate endDate);

    long countByStatus(LeaveStatus status);

    long countByLeaveType(LeaveType leaveType);
}
