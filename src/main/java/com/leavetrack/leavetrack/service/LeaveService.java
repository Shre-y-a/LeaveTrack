package com.leavetrack.leavetrack.service;

import org.springframework.transaction.annotation.Transactional;
import com.leavetrack.leavetrack.entity.Employee;
import com.leavetrack.leavetrack.entity.LeaveRequest;
import com.leavetrack.leavetrack.entity.LeaveStatus;
import com.leavetrack.leavetrack.entity.LeaveType;
import com.leavetrack.leavetrack.exception.BadRequestException;
import com.leavetrack.leavetrack.exception.ResourceNotFoundException;
import com.leavetrack.leavetrack.repository.EmployeeRepository;
import com.leavetrack.leavetrack.repository.LeaveRequestRepository;
import com.leavetrack.leavetrack.repository.LeaveTypeRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class LeaveService {

    private final EmployeeRepository employeeRepository;
    private final LeaveTypeRepository leaveTypeRepository;
    private final LeaveRequestRepository leaveRequestRepository;

    public LeaveService(EmployeeRepository employeeRepository,
                        LeaveTypeRepository leaveTypeRepository,
                        LeaveRequestRepository leaveRequestRepository) {
        this.employeeRepository = employeeRepository;
        this.leaveTypeRepository = leaveTypeRepository;
        this.leaveRequestRepository = leaveRequestRepository;
    }

    public LeaveRequest applyForLeave(Long employeeId, Long leaveTypeId,
                                      LocalDate startDate, LocalDate endDate,
                                      String reason) {

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

        LeaveType leaveType = leaveTypeRepository.findById(leaveTypeId)
                .orElseThrow(() -> new ResourceNotFoundException("Leave type not found"));

        if (endDate.isBefore(startDate)) {
            throw new BadRequestException("End date cannot be before start date");
        }

        long days = ChronoUnit.DAYS.between(startDate, endDate) + 1;

        if (days > employee.getLeaveBalance()) {
            throw new BadRequestException("Not enough leave balance. Requested: "
                    + days + ", available: " + employee.getLeaveBalance());
        }

        LeaveRequest request = new LeaveRequest(employee, leaveType, startDate, endDate, reason);
        return leaveRequestRepository.save(request);
    }

    public List<LeaveRequest> getRequestsForEmployee(Long employeeId) {
        return leaveRequestRepository.findByEmployeeId(employeeId);
    }

    public List<LeaveRequest> getPendingRequests() {
        return leaveRequestRepository.findByStatus(LeaveStatus.PENDING);
    }
        @Transactional
    public LeaveRequest approveRequest(Long requestId) {
        LeaveRequest request = leaveRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Leave request not found"));

        if (request.getStatus() != LeaveStatus.PENDING) {
            throw new BadRequestException("Only PENDING requests can be approved");
        }

        Employee employee = request.getEmployee();
        long days = ChronoUnit.DAYS.between(request.getStartDate(), request.getEndDate()) + 1;

        if (days > employee.getLeaveBalance()) {
            throw new BadRequestException("Employee no longer has enough leave balance");
        }

        employee.setLeaveBalance(employee.getLeaveBalance() - (int) days);
        request.setStatus(LeaveStatus.APPROVED);

        employeeRepository.save(employee);
        return leaveRequestRepository.save(request);
    }

    @Transactional
    public LeaveRequest rejectRequest(Long requestId) {
        LeaveRequest request = leaveRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Leave request not found"));

        if (request.getStatus() != LeaveStatus.PENDING) {
            throw new BadRequestException("Only PENDING requests can be rejected");
        }

        request.setStatus(LeaveStatus.REJECTED);
        return leaveRequestRepository.save(request);
    }
}