package com.example.services.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class EmployeeDto {
	private long id;
	private String employeeName;
	private String employeeEmail;
	private String employeeBaseLocation;
	private Long projectCode;
	private String projectName;
     // exclude password in response
	//private String password;
}
