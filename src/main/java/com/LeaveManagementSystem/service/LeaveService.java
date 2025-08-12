package com.LeaveManagementSystem.service;

import com.LeaveManagementSystem.dto.LeaveRequestDTO;
import com.LeaveManagementSystem.exception.EmployeeNotFoundException;
import com.LeaveManagementSystem.exception.InvalidLeaveRequestException;
import com.LeaveManagementSystem.model.Employee;
import com.LeaveManagementSystem.model.LeaveRequest;
import com.LeaveManagementSystem.model.LeaveStatus;
import com.LeaveManagementSystem.repository.EmployeeRepository;
import com.LeaveManagementSystem.repository.LeaveRequestRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LeaveService {

    @Autowired
    private LeaveRequestRepository leaveRequestRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    public LeaveRequestDTO applyForLeave(LeaveRequestDTO leaveRequestDTO) {
        // Validate employee exists
        Employee employee = employeeRepository.findById(leaveRequestDTO.getEmployeeId())
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with ID: " +
                        leaveRequestDTO.getEmployeeId()));

        // Validate dates
        validateDates(leaveRequestDTO.getStartDate(), leaveRequestDTO.getEndDate());

        // Calculate days
        int days = calculateLeaveDays(leaveRequestDTO.getStartDate(), leaveRequestDTO.getEndDate());

        // Validate against joining date
        if (leaveRequestDTO.getStartDate().isBefore(employee.getJoiningDate())) {
            throw new InvalidLeaveRequestException("Cannot apply for leave before joining date: " +
                    employee.getJoiningDate());
        }

        // Check leave balance
        validateLeaveBalance(employee.getId(), days);

        // Check overlapping requests
        validateOverlappingRequests(employee.getId(), leaveRequestDTO.getStartDate(),
                leaveRequestDTO.getEndDate());

        LeaveRequest leaveRequest = new LeaveRequest(
                leaveRequestDTO.getEmployeeId(),
                leaveRequestDTO.getStartDate(),
                leaveRequestDTO.getEndDate(),
                days,
                leaveRequestDTO.getReason()
        );

        LeaveRequest savedRequest = leaveRequestRepository.save(leaveRequest);
        return convertToDTO(savedRequest);
    }

    public LeaveRequestDTO approveLeave(Long leaveRequestId) {
        LeaveRequest leaveRequest = leaveRequestRepository.findById(leaveRequestId)
                .orElseThrow(() -> new InvalidLeaveRequestException("Leave request not found with ID: " +
                        leaveRequestId));

        if (leaveRequest.getStatus() != LeaveStatus.PENDING) {
            throw new InvalidLeaveRequestException("Leave request is already " +
                    leaveRequest.getStatus().toString().toLowerCase());
        }

        // Double-check leave balance at approval time
        validateLeaveBalance(leaveRequest.getEmployeeId(), leaveRequest.getDays());

        leaveRequest.setStatus(LeaveStatus.APPROVED);
        LeaveRequest savedRequest = leaveRequestRepository.save(leaveRequest);
        return convertToDTO(savedRequest);
    }

    public LeaveRequestDTO rejectLeave(Long leaveRequestId) {
        LeaveRequest leaveRequest = leaveRequestRepository.findById(leaveRequestId)
                .orElseThrow(() -> new InvalidLeaveRequestException("Leave request not found with ID: " +
                        leaveRequestId));

        if (leaveRequest.getStatus() != LeaveStatus.PENDING) {
            throw new InvalidLeaveRequestException("Leave request is already " +
                    leaveRequest.getStatus().toString().toLowerCase());
        }

        leaveRequest.setStatus(LeaveStatus.REJECTED);
        LeaveRequest savedRequest = leaveRequestRepository.save(leaveRequest);
        return convertToDTO(savedRequest);
    }

    public List<LeaveRequestDTO> getAllLeaveRequests() {
        return leaveRequestRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<LeaveRequestDTO> getLeaveRequestsByEmployee(Long employeeId) {
        // Validate employee exists
        employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with ID: " + employeeId));

        return leaveRequestRepository.findByEmployeeId(employeeId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private void validateDates(LocalDate startDate, LocalDate endDate) {
        if (startDate.isAfter(endDate)) {
            throw new InvalidLeaveRequestException("Start date cannot be after end date");
        }

        if (startDate.isBefore(LocalDate.now())) {
            throw new InvalidLeaveRequestException("Cannot apply for leave for past dates");
        }

        // Check if dates are too far in future (6 months limit)
        if (startDate.isAfter(LocalDate.now().plusMonths(6))) {
            throw new InvalidLeaveRequestException("Cannot apply for leave more than 6 months in advance");
        }
    }

    private int calculateLeaveDays(LocalDate startDate, LocalDate endDate) {

        int startDay = startDate.getDayOfYear();
        int endDay = endDate.getDayOfYear();
        int startYear = startDate.getYear();
        int endYear = endDate.getYear();

        if (startYear == endYear) {
            return endDay - startDay + 1;
        } else {
            // For different years - simple case
            return endDay - startDay + 1; // Simplified for same year
        }
    }

    private void validateLeaveBalance(Long employeeId, int requestedDays) {
        Employee employee = employeeRepository.findById(employeeId).get();
        int currentYear = LocalDate.now().getYear();
        Integer usedLeaves = leaveRequestRepository.getTotalApprovedLeaveDaysForYear(employeeId, currentYear);

        if (usedLeaves == null) {
            usedLeaves = 0;
        }

        int availableBalance = employee.getAnnualLeaveBalance() - usedLeaves;

        if (requestedDays > availableBalance) {
            throw new InvalidLeaveRequestException(
                    "Insufficient leave balance. Available: " + availableBalance +
                            ", Requested: " + requestedDays);
        }
    }

    private void validateOverlappingRequests(Long employeeId, LocalDate startDate, LocalDate endDate) {
        List<LeaveRequest> overlappingRequests = leaveRequestRepository
                .findOverlappingApprovedLeaves(employeeId, startDate, endDate);

        if (!overlappingRequests.isEmpty()) {
            throw new InvalidLeaveRequestException("Overlapping leave request found for the given dates");
        }
    }

    private LeaveRequestDTO convertToDTO(LeaveRequest leaveRequest) {
        LeaveRequestDTO dto = new LeaveRequestDTO();
        dto.setId(leaveRequest.getId());
        dto.setEmployeeId(leaveRequest.getEmployeeId());
        dto.setStartDate(leaveRequest.getStartDate());
        dto.setEndDate(leaveRequest.getEndDate());
        dto.setDays(leaveRequest.getDays());
        dto.setReason(leaveRequest.getReason());
        dto.setStatus(leaveRequest.getStatus());
        dto.setCreatedAt(leaveRequest.getCreatedAt());
        dto.setUpdatedAt(leaveRequest.getUpdatedAt());
        return dto;
    }
}
