package com.example.services.service;

import org.springframework.data.domain.*;

import com.example.services.entity.Project;

public interface ProjectService {
	public Project saveProject(Project project);

	public Project getProjectByCode(long projectCode);

	public Page<Project> getAllProjects(Pageable pageable);

	Project updateProject(Long projectCode, Project project);

	void deleteProject(Long projectCode);
}
