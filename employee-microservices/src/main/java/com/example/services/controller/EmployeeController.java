package com.example.services.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.services.entity.Employee;
import com.example.services.payload.EmployeeDto;
import com.example.services.service.EmployeeService;

@RestController
@RequestMapping("/api/employee")
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;

	// save a new employee
	@PostMapping
	public EmployeeDto createEmployee(@RequestBody Employee employee) {
		return employeeService.saveEmployee(employee);
	}

	// get or fetch an employee by id
	@GetMapping("/{employee_id}")
	public EmployeeDto getEmployeeById(@PathVariable long employee_id) {
		return employeeService.getEmployeeById(employee_id);
	}

	// get employees by project code - One project to many employees
	@GetMapping("/project/{projectCode}")
	public Page<Employee> getEmployeesByProjectCode(@PathVariable Long projectCode, Pageable pageable) {
		return employeeService.getEmployeesByAssignedProjectCode(projectCode, pageable);
	}

	// Commented to fix the response returned. Changed return type to EmployeeDto to include projectName associated with an employee
//	// fetch all employees in the database
//	@GetMapping("/allemployees")
//	public Page<Employee> getAllEmployees( Pageable pageable) {
//		return employeeService.getAllEmployees(pageable);
//	}
	
	//updated
	@GetMapping("/allemployees")
	public Page<EmployeeDto> getAllEmployees(Pageable pageable) {
	    return employeeService.getAllEmployees(pageable);
	}

	// update an employee by id
	@PutMapping("/{id}")
	public EmployeeDto updateEmployee(@PathVariable Long id, @RequestBody Employee employee) {
		return employeeService.updateEmployee(id, employee);
	}

	// delete an employee by id
	@DeleteMapping("/{id}")
	public String deleteEmployee(@PathVariable Long id) {
		employeeService.deleteEmployee(id);
		return "Employee deleted successfully";
	}
	
	// fetch/get an employee by email for (Auth service login)login purpose
	@GetMapping("/email/{email}")
	public Employee getEmployeeByEmail(@PathVariable String email) {
	    return employeeService.getEmployeeByEmail(email);
	}
	
}
