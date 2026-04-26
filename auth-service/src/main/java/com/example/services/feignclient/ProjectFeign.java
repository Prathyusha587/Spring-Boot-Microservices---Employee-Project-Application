package com.example.services.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import feign.Response;

@FeignClient(name = "project-microservices")
public interface ProjectFeign {
    @GetMapping("/api/project/{projectCode}")
    Response getProjectByCode(@PathVariable("projectCode") Long projectCode);
}