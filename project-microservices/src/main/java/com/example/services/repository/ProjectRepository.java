package com.example.services.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.services.entity.Project;

public interface ProjectRepository extends JpaRepository<Project, Long> {
	Project findByProjectCode(long employeeAssignedProject);

}
