package com.example.services.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(url = "http://localhost:8081", value = "project-feign-client", path = "/api/project")
public interface ProjectFeign {
	@GetMapping("/{project_code}")
	feign.Response getProjectsByCode(@PathVariable long project_code);

	@GetMapping("/by-email/{employeeEmail}")
	feign.Response getEmployeeByEmail(@PathVariable("employeeEmail") String employeeEmail);
}