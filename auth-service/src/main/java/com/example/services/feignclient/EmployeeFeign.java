package com.example.services.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "employee-service", url = "http://localhost:8083")
public interface EmployeeFeign {

    @GetMapping("/api/employee/email/{email}")
    feign.Response getEmployeeByEmail(@PathVariable("email") String email);
}