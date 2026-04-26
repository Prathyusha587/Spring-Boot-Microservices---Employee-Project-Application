package com.example.services.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.services.feignclient.EmployeeFeign;
import com.example.services.feignclient.ProjectFeign;
import com.example.services.payload.Employee;
import com.example.services.payload.Project;
import com.example.services.util.JwtUtil;
import com.google.gson.Gson;

import feign.Response;

@Service
public class AuthService {

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private EmployeeFeign employeeFeign;
	
	@Autowired
	private ProjectFeign projectFeign;

	@SuppressWarnings("static-access")
	public Map<String, Object> login(String employeeEmail, String password) {
		// Use try-with-resources to ensure response body is closed properly
		try (Response response = employeeFeign.getEmployeeByEmail(employeeEmail)) {

			System.out.println("Feign Status: " + response.status());
			if (response.status() != 200) {
				throw new RuntimeException("Employee not found or service error");
			}

			// Read response body as string
			String body = new String(response.body().asInputStream().readAllBytes());

			// Convert JSON to Employee object using Gson
			Gson g = new Gson();
			Employee employee = g.fromJson(body, Employee.class);

			// Check if employee exists and password matches
			if (employee == null || !password.equals(employee.getPassword())) {
				throw new RuntimeException("Invalid credentials");
			}

			// Generate JWT token
			String employeeToken = jwtUtil.generateToken(employee.getEmployeeEmail());

			// Prepare response map with employee details + token (excluding password)
			Map<String, Object> employeeAuthResult = new HashMap<>();
			employeeAuthResult.put("employeeId", employee.getId());
			employeeAuthResult.put("employeeName", employee.getEmployeeName());
			employeeAuthResult.put("employeeEmail", employee.getEmployeeEmail());
			employeeAuthResult.put("employeeBaseLocation", employee.getEmployeeBaseLocation());
			employeeAuthResult.put("projectCode", employee.getEmployeeAssignedProject());
			employeeAuthResult.put("token", employeeToken);

			 // to fetch project details and add to response
            try (Response projectResponse = projectFeign.getProjectByCode(employee.getEmployeeAssignedProject())) {
                System.out.println("Project Feign Status: " + projectResponse.status());
                if (projectResponse.status() == 200) {
                    String projectBody = new String(projectResponse.body().asInputStream().readAllBytes());
                    Project project = g.fromJson(projectBody, Project.class);
                    employeeAuthResult.put("projectDetails", project);
                }
            }

            return employeeAuthResult;
			

		} catch (IOException e) {
			throw new RuntimeException("Error while fetching employee service response", e);
		}
	}
	
}