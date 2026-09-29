package com.leavetrack.leavetrack.config;

import com.leavetrack.leavetrack.entity.Employee;
import com.leavetrack.leavetrack.entity.LeaveType;
import com.leavetrack.leavetrack.entity.Role;
import com.leavetrack.leavetrack.repository.EmployeeRepository;
import com.leavetrack.leavetrack.repository.LeaveTypeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final LeaveTypeRepository leaveTypeRepository;
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;

    public DataLoader(LeaveTypeRepository leaveTypeRepository,
                      EmployeeRepository employeeRepository,
                      PasswordEncoder passwordEncoder) {
        this.leaveTypeRepository = leaveTypeRepository;
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (leaveTypeRepository.count() == 0) {
            leaveTypeRepository.save(new LeaveType("Casual", 12));
            leaveTypeRepository.save(new LeaveType("Sick", 10));
            leaveTypeRepository.save(new LeaveType("Earned", 15));
        }

        if (employeeRepository.count() == 0) {
            employeeRepository.save(new Employee("Manager One", "manager@leavetrack.com",
                    passwordEncoder.encode("temp123"), Role.MANAGER, 20));
            employeeRepository.save(new Employee("Employee One", "employee@leavetrack.com",
                    passwordEncoder.encode("temp123"), Role.EMPLOYEE, 20));
        }
    }
}