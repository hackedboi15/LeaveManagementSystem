package com.LeaveManagementSystem.controller;


import com.LeaveManagementSystem.dto.LeaveRequestDTO;
import com.LeaveManagementSystem.service.LeaveService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/leave-requests")
@CrossOrigin(origins = "*")
public class LeaveController {

    @Autowired
    private LeaveService leaveService;

    @PostMapping
    public ResponseEntity<LeaveRequestDTO> applyForLeave(@Valid @RequestBody LeaveRequestDTO leaveRequestDTO) {
        LeaveRequestDTO createdRequest = leaveService.applyForLeave(leaveRequestDTO);
        return new ResponseEntity<>(createdRequest, HttpStatus.CREATED);
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<LeaveRequestDTO> approveLeave(@PathVariable Long id) {
        LeaveRequestDTO approvedRequest = leaveService.approveLeave(id);
        return new ResponseEntity<>(approvedRequest, HttpStatus.OK);
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<LeaveRequestDTO> rejectLeave(@PathVariable Long id) {
        LeaveRequestDTO rejectedRequest = leaveService.rejectLeave(id);
        return new ResponseEntity<>(rejectedRequest, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<LeaveRequestDTO>> getAllLeaveRequests() {
        List<LeaveRequestDTO> requests = leaveService.getAllLeaveRequests();
        return new ResponseEntity<>(requests, HttpStatus.OK);
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<LeaveRequestDTO>> getLeaveRequestsByEmployee(@PathVariable Long employeeId) {
        List<LeaveRequestDTO> requests = leaveService.getLeaveRequestsByEmployee(employeeId);
        return new ResponseEntity<>(requests, HttpStatus.OK);
    }
}
