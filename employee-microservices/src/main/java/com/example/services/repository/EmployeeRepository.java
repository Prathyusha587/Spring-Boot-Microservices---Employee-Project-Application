package com.example.services.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.services.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
	// Fetch all employees assigned to a particular project ( One Project -> Many
	// Employees)
	Page<Employee> findByEmployeeAssignedProject(Long employeeAssignedProject, Pageable pageable);
	Optional<Employee> findByEmployeeEmail(String employeeEmail);
	Page<Employee> findAllByOrderByIdDesc(Pageable pageable);
}
