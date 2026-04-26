package com.example.services.serviceImpl;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.services.entity.Employee;
import com.example.services.entity.Project;
import com.example.services.exceptions.ResourceNotFoundException;
import com.example.services.feignclient.ProjectFeign;
import com.example.services.payload.EmployeeDto;
import com.example.services.repository.EmployeeRepository;
import com.example.services.service.EmployeeService;
import com.google.gson.Gson;

import feign.Response;

@Service
public class EmployeeServiceImpl implements EmployeeService {

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private ProjectFeign projectFeign;

	@Override
	public EmployeeDto saveEmployee(Employee employee) {

		Employee savedEmployee = employeeRepository.save(employee);

		Response response = projectFeign.getProjectsByCode(savedEmployee.getEmployeeAssignedProject());

		String body = "";
		try {
			body = new String(response.body().asInputStream().readAllBytes());
		} catch (IOException e) {
			throw new RuntimeException("Error while fetching project service response");
		}

		Gson g = new Gson();
		Project project = g.fromJson(body, Project.class);

		if (project == null) {
			throw new ResourceNotFoundException(
					"Project not found with code: " + savedEmployee.getEmployeeAssignedProject());
		}

		EmployeeDto employeeDto = new EmployeeDto();
		employeeDto.setId(savedEmployee.getId());
		employeeDto.setEmployeeName(savedEmployee.getEmployeeName());
		employeeDto.setEmployeeEmail(savedEmployee.getEmployeeEmail());
		employeeDto.setEmployeeBaseLocation(savedEmployee.getEmployeeBaseLocation());
		employeeDto.setProjectCode(project.getProjectCode());
		employeeDto.setProjectName(project.getProjectName());
		

		return employeeDto;
	}

	@Override
	public EmployeeDto getEmployeeById(long id) {

		Employee foundEmployee = employeeRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));

		Response response = projectFeign.getProjectsByCode(foundEmployee.getEmployeeAssignedProject());

		String body = "";
		try {
			body = new String(response.body().asInputStream().readAllBytes());
		} catch (IOException e) {
			throw new RuntimeException("Error while reading project service response");
		}

		Gson g = new Gson();
		Project project = g.fromJson(body, Project.class);

		if (project == null) {
			throw new ResourceNotFoundException(
					"Project not found with code: " + foundEmployee.getEmployeeAssignedProject());
		}

		EmployeeDto employeeDto = new EmployeeDto();
		employeeDto.setId(foundEmployee.getId());
		employeeDto.setEmployeeName(foundEmployee.getEmployeeName());
		employeeDto.setEmployeeEmail(foundEmployee.getEmployeeEmail());
		employeeDto.setEmployeeBaseLocation(foundEmployee.getEmployeeBaseLocation());
		employeeDto.setProjectCode(project.getProjectCode());
		employeeDto.setProjectName(project.getProjectName());

		return employeeDto;
	}

	@Override
	public Page<Employee> getEmployeesByAssignedProjectCode(Long projectCode, Pageable pageable) {

		Response response = projectFeign.getProjectsByCode(projectCode);

		String body = "";
		try {
			body = new String(response.body().asInputStream().readAllBytes());
		} catch (IOException e) {
			throw new RuntimeException("Error while reading project service response");
		}

		Gson g = new Gson();
		Project project = g.fromJson(body, Project.class);

		if (project == null) {
			throw new ResourceNotFoundException("Project not found with code: " + projectCode);
		}

		return employeeRepository.findByEmployeeAssignedProject(projectCode, pageable);
	}

// Commented to fix the response returned. Changed return type to EmployeeDto to include projectName associated with an employee
//	@Override
//	public Page<Employee> getAllEmployees(Pageable pageable) {
//
//		return employeeRepository.findAll(pageable);
//	}

	@Override
	public Page<EmployeeDto> getAllEmployees(Pageable pageable) {

		// Commented to display recently added data first on ui
//	    Page<Employee> employees = employeeRepository.findAll(pageable);
		Page<Employee> employees =
		        employeeRepository.findAllByOrderByIdDesc(pageable);

	    return employees.map(employee -> {
	        EmployeeDto dto = new EmployeeDto();
	        dto.setId(employee.getId());
	        dto.setEmployeeName(employee.getEmployeeName());
	        dto.setEmployeeEmail(employee.getEmployeeEmail());
	        dto.setEmployeeBaseLocation(employee.getEmployeeBaseLocation());

	        try {
	            Response response = projectFeign.getProjectsByCode(employee.getEmployeeAssignedProject());
	            String body = new String(response.body().asInputStream().readAllBytes());
	            Project project = new Gson().fromJson(body, Project.class);

	            if (project != null) {
	                dto.setProjectCode(project.getProjectCode());
	                dto.setProjectName(project.getProjectName());
	            } else {
	                dto.setProjectName("N/A");
	            }
	        } catch (Exception e) {
	            dto.setProjectName("N/A");
	        }

	        return dto;
	    });
	}
	
	// commented to fix edit employee failure
//	@Override
//	public EmployeeDto updateEmployee(Long id, Employee employee) {
//	    Employee existingEmployee = employeeRepository.findById(id)
//	            .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
//
//	    // Update basic fields
//	    existingEmployee.setEmployeeName(employee.getEmployeeName());
//	    existingEmployee.setEmployeeEmail(employee.getEmployeeEmail());
//	    existingEmployee.setEmployeeAssignedProject(employee.getEmployeeAssignedProject());
//	    existingEmployee.setEmployeeBaseLocation(employee.getEmployeeBaseLocation());
//
//	    // 🔥 Update password ONLY if provided
//	    if (employee.getPassword() != null && !employee.getPassword().isEmpty()) {
//	        existingEmployee.setPassword(employee.getPassword());
//	    }
//
//	    Employee updatedEmployee = employeeRepository.save(existingEmployee);
//
//	    // Fetch project details
//	    Response response = projectFeign.getProjectsByCode(updatedEmployee.getEmployeeAssignedProject());
//
//	    String body = "";
//	    try {
//	        body = new String(response.body().asInputStream().readAllBytes());
//	    } catch (IOException e) {
//	        throw new RuntimeException("Error while fetching project details");
//	    }
//
//	    Gson g = new Gson();
//	    Project project = g.fromJson(body, Project.class);
//
//	    if (project == null) {
//	        throw new ResourceNotFoundException(
//	                "Project not found with code: " + updatedEmployee.getEmployeeAssignedProject());
//	    }
//
//	    // Prepare DTO response
//	    EmployeeDto employeeDto = new EmployeeDto();
//	    employeeDto.setId(updatedEmployee.getId());
//	    employeeDto.setEmployeeName(updatedEmployee.getEmployeeName());
//	    employeeDto.setEmployeeEmail(updatedEmployee.getEmployeeEmail());
//	    employeeDto.setEmployeeBaseLocation(updatedEmployee.getEmployeeBaseLocation());
//	    employeeDto.setProjectCode(project.getProjectCode());
//	    employeeDto.setProjectName(project.getProjectName());
//
//	    return employeeDto;
//	    
//	   
//	}
	
	@Override
	public EmployeeDto updateEmployee(Long id, Employee employee) {

	    Employee existingEmployee = employeeRepository.findById(id)
	            .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));

	    // 1. Update employee fields
	    existingEmployee.setEmployeeName(employee.getEmployeeName());
	    existingEmployee.setEmployeeEmail(employee.getEmployeeEmail());
	    existingEmployee.setEmployeeAssignedProject(employee.getEmployeeAssignedProject());
	    existingEmployee.setEmployeeBaseLocation(employee.getEmployeeBaseLocation());

	    if (employee.getPassword() != null && !employee.getPassword().isEmpty()) {
	        existingEmployee.setPassword(employee.getPassword());
	    }

	    // 2. Save employee
	    Employee updatedEmployee = employeeRepository.save(existingEmployee);

	    // 3. Call project service
	    Response response = projectFeign.getProjectsByCode(updatedEmployee.getEmployeeAssignedProject());

	    String body;
	    try {
	        body = new String(response.body().asInputStream().readAllBytes());
	    } catch (IOException e) {
	        throw new RuntimeException("Error while fetching project details");
	    }

	    // 4. SAFE CHECK (THIS FIXES YOUR ERROR)
	    if (body == null || body.isBlank() || !body.trim().startsWith("{")) {
	        throw new RuntimeException("Invalid project response from service: " + body);
	    }

	    Gson g = new Gson();
	    Project project;

	    try {
	        project = g.fromJson(body, Project.class);
	    } catch (Exception e) {
	        throw new RuntimeException("Failed to parse project JSON: " + body);
	    }

	    if (project == null) {
	        throw new ResourceNotFoundException(
	                "Project not found with code: " + updatedEmployee.getEmployeeAssignedProject());
	    }

	    // 5. Build DTO response
	    EmployeeDto dto = new EmployeeDto();
	    dto.setId(updatedEmployee.getId());
	    dto.setEmployeeName(updatedEmployee.getEmployeeName());
	    dto.setEmployeeEmail(updatedEmployee.getEmployeeEmail());
	    dto.setEmployeeBaseLocation(updatedEmployee.getEmployeeBaseLocation());
	    dto.setProjectCode(updatedEmployee.getEmployeeAssignedProject());
	    dto.setProjectName(project.getProjectName());

	    return dto;
	}
	
	@Override
	public void deleteEmployee(Long id) {
		Employee employee = employeeRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));

		employeeRepository.delete(employee);

	}

	@Override
	public Employee getEmployeeByEmail(String employeeEmail) {
	    return employeeRepository.findByEmployeeEmail(employeeEmail)
	            .orElseThrow(() -> new ResourceNotFoundException(
	                "Employee not found with email: " + employeeEmail));
	}

}