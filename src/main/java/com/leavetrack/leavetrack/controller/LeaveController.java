package com.leavetrack.leavetrack.controller;

import com.leavetrack.leavetrack.dto.LeaveApplyRequest;
import com.leavetrack.leavetrack.dto.LeaveResponse;
import com.leavetrack.leavetrack.entity.Employee;
import com.leavetrack.leavetrack.entity.LeaveRequest;
import com.leavetrack.leavetrack.exception.ResourceNotFoundException;
import com.leavetrack.leavetrack.repository.EmployeeRepository;
import com.leavetrack.leavetrack.service.LeaveService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/leave")
public class LeaveController {

    private final LeaveService leaveService;
    private final EmployeeRepository employeeRepository;

    public LeaveController(LeaveService leaveService, EmployeeRepository employeeRepository) {
        this.leaveService = leaveService;
        this.employeeRepository = employeeRepository;
    }

    // Employee applies for leave
    @PostMapping("/apply")
    @Transactional
    public ResponseEntity<LeaveResponse> apply(@Valid @RequestBody LeaveApplyRequest req) {
        LeaveRequest saved = leaveService.applyForLeave(
                req.getEmployeeId(), req.getLeaveTypeId(),
                req.getStartDate(), req.getEndDate(), req.getReason());
        return ResponseEntity.status(HttpStatus.CREATED).body(new LeaveResponse(saved));
    }

    // Employee views their balance
    @GetMapping("/balance/{employeeId}")
    public Map<String, Object> balance(@PathVariable Long employeeId) {
        Employee e = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
        return Map.of("employee", e.getName(), "leaveBalance", e.getLeaveBalance());
    }

    // Employee views their request history
    @GetMapping("/employee/{employeeId}")
    @Transactional(readOnly = true)
    public List<LeaveResponse> history(@PathVariable Long employeeId) {
        return leaveService.getRequestsForEmployee(employeeId).stream()
                .map(LeaveResponse::new).toList();
    }

    // Manager views pending requests
    @GetMapping("/pending")
    @Transactional(readOnly = true)
    public List<LeaveResponse> pending() {
        return leaveService.getPendingRequests().stream()
                .map(LeaveResponse::new).toList();
    }

    // Manager approves
    @PutMapping("/{id}/approve")
    @Transactional
    public LeaveResponse approve(@PathVariable Long id) {
        return new LeaveResponse(leaveService.approveRequest(id));
    }

    // Manager rejects
    @PutMapping("/{id}/reject")
    @Transactional
    public LeaveResponse reject(@PathVariable Long id) {
        return new LeaveResponse(leaveService.rejectRequest(id));
    }
}