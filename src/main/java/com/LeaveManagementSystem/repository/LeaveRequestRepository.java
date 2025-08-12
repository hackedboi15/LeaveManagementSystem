package com.LeaveManagementSystem.repository;


import com.LeaveManagementSystem.model.LeaveRequest;
import com.LeaveManagementSystem.model.LeaveStatus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {

    List<LeaveRequest> findByEmployeeId(Long employeeId);

    List<LeaveRequest> findByEmployeeIdAndStatus(Long employeeId, LeaveStatus status);

    @Query("SELECT lr FROM LeaveRequest lr WHERE lr.employeeId = :employeeId " +
            "AND lr.status = 'APPROVED' " +
            "AND ((lr.startDate <= :endDate AND lr.endDate >= :startDate))")
    List<LeaveRequest> findOverlappingApprovedLeaves(
            @Param("employeeId") Long employeeId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query("SELECT SUM(lr.days) FROM LeaveRequest lr WHERE lr.employeeId = :employeeId " +
            "AND lr.status = 'APPROVED' " +
            "AND YEAR(lr.startDate) = :year")
    Integer getTotalApprovedLeaveDaysForYear(@Param("employeeId") Long employeeId,
                                             @Param("year") int year);
}

