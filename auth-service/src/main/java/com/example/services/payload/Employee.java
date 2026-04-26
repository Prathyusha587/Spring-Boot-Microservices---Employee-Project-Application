package com.example.services.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class Employee {
	private long id;
	private String employeeName;
	private String employeeEmail;
	private long employeeAssignedProject;
	private String employeeBaseLocation;
	private String password;
}
