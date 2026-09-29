package com.leavetrack.leavetrack.dto;

import com.leavetrack.leavetrack.entity.LeaveRequest;

import java.time.LocalDate;

public class LeaveResponse {

    private Long id;
    private String employeeName;
    private String leaveType;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
    private String reason;

    public LeaveResponse(LeaveRequest r) {
        this.id = r.getId();
        this.employeeName = r.getEmployee().getName();
        this.leaveType = r.getLeaveType().getName();
        this.startDate = r.getStartDate();
        this.endDate = r.getEndDate();
        this.status = r.getStatus().name();
        this.reason = r.getReason();
    }

    public Long getId() { return id; }
    public String getEmployeeName() { return employeeName; }
    public String getLeaveType() { return leaveType; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public String getStatus() { return status; }
    public String getReason() { return reason; }
}