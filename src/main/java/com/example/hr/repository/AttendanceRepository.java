package com.example.hr.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.hr.entity.Attendance;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findByEmployeeId(Long employeeId);

    List<Attendance> findByWorkDate(LocalDate workDate);

    Optional<Attendance> findByEmployeeIdAndWorkDate(Long employeeId, LocalDate workDate);

    List<Attendance> findByWorkDateBetween(LocalDate startDate, LocalDate endDate);

    long countByWorkDate(LocalDate workDate);

    long countByWorkDateBetween(LocalDate startDate, LocalDate endDate);
}
