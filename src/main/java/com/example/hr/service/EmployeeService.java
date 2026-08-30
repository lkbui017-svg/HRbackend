package com.example.hr.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.hr.dto.EmployeeRequest;
import com.example.hr.dto.EmployeeResponse;
import com.example.hr.entity.Employee;
import com.example.hr.entity.EmployeeStatus;
import com.example.hr.exception.ResourceNotFoundException;
import com.example.hr.repository.EmployeeRepository;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;

    public List<EmployeeResponse> getAllEmployees() {
        return employeeRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public EmployeeResponse getEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found: " + id));
        return toResponse(employee);
    }

    public EmployeeResponse createEmployee(EmployeeRequest request) {
        if (employeeRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email already in use: " + request.email());
        }

        Employee employee = new Employee(
                request.fullName(),
                request.department(),
                request.position(),
                request.email(),
                request.phone(),
                EmployeeStatus.ACTIVE,
                request.hireDate(),
                request.contractType(),
                request.salary()
        );

        Employee saved = employeeRepository.save(employee);
        return toResponse(saved);
    }

    public EmployeeResponse updateEmployee(Long id, EmployeeRequest request) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found: " + id));

        employee.setFullName(request.fullName());
        employee.setDepartment(request.department());
        employee.setPosition(request.position());
        employee.setPhone(request.phone());
        employee.setContractType(request.contractType());
        employee.setSalary(request.salary());

        Employee updated = employeeRepository.save(employee);
        return toResponse(updated);
    }

    public void deleteEmployee(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found: " + id));
        employee.setStatus(EmployeeStatus.RESIGNED);
        employeeRepository.save(employee);
    }

    public List<EmployeeResponse> getEmployeesByDepartment(String department) {
        return employeeRepository.findByDepartment(department).stream()
                .map(this::toResponse)
                .toList();
    }

    private EmployeeResponse toResponse(Employee employee) {
        return new EmployeeResponse(
                employee.getId(),
                employee.getFullName(),
                employee.getDepartment(),
                employee.getPosition(),
                employee.getEmail(),
                employee.getPhone(),
                employee.getStatus().toString(),
                employee.getHireDate(),
                employee.getContractType(),
                employee.getSalary()
        );
    }
}
