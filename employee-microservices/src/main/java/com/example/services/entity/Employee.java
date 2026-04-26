package com.example.services.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "employee", uniqueConstraints = { @UniqueConstraint(columnNames = { "employee_email" }) })
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String employeeName;

    @Column(nullable = false, name = "employee_email")
    private String employeeEmail;

    @Column(nullable = false)
    private long employeeAssignedProject;

    @Column(nullable = false)
    private String employeeBaseLocation;

    @Column(nullable = true)
    private String password;
}
