package com.example.services.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.services.entity.Employee;
import com.example.services.payload.EmployeeDto;

public interface EmployeeService {
	public EmployeeDto saveEmployee(Employee employee);

	public EmployeeDto getEmployeeById(long id);

	// fetch employees by project
	Page<Employee> getEmployeesByAssignedProjectCode(Long projectCode, Pageable pageable);

// Commented to fix the response returned. Changed return type to EmployeeDto to include projectName associated with an employee
//	// fetch all employees from the database
//	Page<Employee> getAllEmployees(Pageable pageable);
	
	//updated
	Page<EmployeeDto> getAllEmployees(Pageable pageable);
	
	//update an existing employee
	EmployeeDto updateEmployee(Long id, Employee employee);

	//delete an employee by id
	void deleteEmployee(Long id);
	
	// get employee by email
	Employee getEmployeeByEmail(String employeeEmail);
}
