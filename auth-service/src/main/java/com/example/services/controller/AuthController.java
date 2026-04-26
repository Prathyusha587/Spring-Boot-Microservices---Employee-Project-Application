package com.example.services.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.services.service.AuthService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> request) {
        String employeeEmail = request.get("employeeEmail");
        String password = request.get("password");

        // Validate input
        if (employeeEmail == null || employeeEmail.isEmpty() || password == null || password.isEmpty()) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", "Email and password are required");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        // Attempt login
        Map<String, Object> response = authService.login(employeeEmail, password);

        // Check response status from service
        if ("error".equals(response.get("status"))) {
            // Unauthorized if email exists but password is wrong
            if ("Incorrect password".equals(response.get("message"))) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
            // Not found if employee doesn't exist
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        // Success
        return ResponseEntity.ok(response);
    }
}