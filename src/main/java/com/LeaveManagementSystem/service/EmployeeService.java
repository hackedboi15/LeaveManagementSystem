package com.LeaveManagementSystem.service;



import com.LeaveManagementSystem.dto.LeaveBalanceDTO;
import com.LeaveManagementSystem.exception.EmployeeNotFoundException;
import com.LeaveManagementSystem.exception.InvalidLeaveRequestException;
import com.LeaveManagementSystem.model.Employee;
import com.LeaveManagementSystem.repository.EmployeeRepository;
import com.LeaveManagementSystem.repository.LeaveRequestRepository;
import com.LeaveManagementSystem.dto.EmployeeDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private LeaveRequestRepository leaveRequestRepository;

    public EmployeeDTO addEmployee(EmployeeDTO employeeDTO) {
        // Validate if email already exists
        if (employeeRepository.existsByEmail(employeeDTO.getEmail())) {
            throw new InvalidLeaveRequestException("Employee with email " +
                    employeeDTO.getEmail() + " already exists");
        }

        // Validate joining date (should not be future date)
        if (employeeDTO.getJoiningDate().isAfter(LocalDate.now())) {
            throw new InvalidLeaveRequestException("Joining date cannot be in future");
        }

        Employee employee = new Employee(
                employeeDTO.getName(),
                employeeDTO.getEmail().toLowerCase().trim(),
                employeeDTO.getDepartment().toUpperCase().trim(),
                employeeDTO.getJoiningDate()
        );

        Employee savedEmployee = employeeRepository.save(employee);
        return convertToDTO(savedEmployee);
    }

    public List<EmployeeDTO> getAllEmployees() {
        return employeeRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public EmployeeDTO getEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with ID: " + id));
        return convertToDTO(employee);
    }

    public LeaveBalanceDTO getLeaveBalance(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with ID: " + employeeId));

        int currentYear = LocalDate.now().getYear();
        Integer usedLeaves = leaveRequestRepository.getTotalApprovedLeaveDaysForYear(employeeId, currentYear);

        if (usedLeaves == null) {
            usedLeaves = 0;
        }

        int remainingBalance = employee.getAnnualLeaveBalance() - usedLeaves;

        return new LeaveBalanceDTO(
                employeeId,
                employee.getName(),
                employee.getAnnualLeaveBalance(),
                usedLeaves,
                remainingBalance
        );
    }

    private EmployeeDTO convertToDTO(Employee employee) {
        EmployeeDTO dto = new EmployeeDTO();
        dto.setId(employee.getId());
        dto.setName(employee.getName());
        dto.setEmail(employee.getEmail());
        dto.setDepartment(employee.getDepartment());
        dto.setJoiningDate(employee.getJoiningDate());
        dto.setAnnualLeaveBalance(employee.getAnnualLeaveBalance());
        return dto;
    }
}
