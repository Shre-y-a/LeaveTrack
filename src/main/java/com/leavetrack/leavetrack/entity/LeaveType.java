package com.leavetrack.leavetrack.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "leave_types")
public class LeaveType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private int maxDaysPerYear;

    // JPA requires an empty constructor
    public LeaveType() {
    }

    public LeaveType(String name, int maxDaysPerYear) {
        this.name = name;
        this.maxDaysPerYear = maxDaysPerYear;
    }

    public Long getId() { return id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getMaxDaysPerYear() { return maxDaysPerYear; }
    public void setMaxDaysPerYear(int maxDaysPerYear) { this.maxDaysPerYear = maxDaysPerYear; }
}