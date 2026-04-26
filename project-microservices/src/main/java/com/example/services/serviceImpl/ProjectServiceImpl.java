package com.example.services.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.services.entity.Project;
import com.example.services.exceptions.ResourceNotFoundException;
import com.example.services.repository.ProjectRepository;
import com.example.services.service.ProjectService;

@Service
public class ProjectServiceImpl implements ProjectService {

	@Autowired
	private ProjectRepository projectRepository;

	@Override
	public Project saveProject(Project project) {
		Project savedProject = projectRepository.save(project);
		return savedProject;
	}

	@Override
	public Project getProjectByCode(long projectCode) {
		Project foundProject = projectRepository.findByProjectCode(projectCode);
		if (foundProject == null) {
			throw new ResourceNotFoundException("Project not found with code: " + projectCode);
		}

		return foundProject;
	}

	@Override
	public Page<Project> getAllProjects(Pageable pageable) {

		return projectRepository.findAll(pageable);
	}

	@Override
	public Project updateProject(Long projectCode, Project project) {

		Project existingProject = projectRepository.findByProjectCode(projectCode);

		if (existingProject == null) {
			throw new ResourceNotFoundException("Project not found with code: " + projectCode);
		}

		existingProject.setProjectName(project.getProjectName());

		return projectRepository.save(existingProject);

	}

	@Override
	public void deleteProject(Long projectCode) {
		Project project = projectRepository.findByProjectCode(projectCode);

		if (project == null) {
			throw new ResourceNotFoundException("Project not found with code: " + projectCode);
		}

		projectRepository.delete(project);
	}

}
