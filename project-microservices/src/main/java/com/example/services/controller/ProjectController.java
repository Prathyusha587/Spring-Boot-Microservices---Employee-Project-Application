package com.example.services.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.services.entity.Project;
import com.example.services.service.ProjectService;

@RestController
@RequestMapping("/api/project")
public class ProjectController {

	@Autowired
	private ProjectService projectService;

	// save a new project
	@PostMapping
	public Project createProject(@RequestBody Project project) {
		return projectService.saveProject(project);
	}

	// fetch a project
	@GetMapping("/{project_code}")
	public Project getProjectsByCode(@PathVariable long project_code) {
		return projectService.getProjectByCode(project_code);
	}

	// fetch all existing projects in the db
	@GetMapping("/allprojects")
	public Page<Project> getAllProjects(Pageable pageable) {
		return projectService.getAllProjects(pageable);

	}

	// update a project
	@PutMapping("/{project_code}")
	public Project updateProject(@PathVariable Long project_code, @RequestBody Project project) {
		return projectService.updateProject(project_code, project);
	}

	// delete a project
	@DeleteMapping("/{project_code}")
	public String deleteProject(@PathVariable Long project_code) {
		projectService.deleteProject(project_code);
		return "Project deleted successfully";
	}
}
