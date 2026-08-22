package com.omar.teamboard.resource;

import com.omar.teamboard.dto.ProjectDto;
import com.omar.teamboard.model.Project;
import com.omar.teamboard.repository.ProjectRepository;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Path("/projects")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProjectResource {

    @Inject
    ProjectRepository projectRepository;

    @GET
    @Path("/team/{teamId}")
    public List<ProjectDto> getTeamProjects(@PathParam("teamId") String teamId) {
        return projectRepository.findByTeamId(teamId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @POST
    public Response createProject(@Valid ProjectDto projectDto) {
        Project project = new Project(UUID.randomUUID().toString(), projectDto.getName(), projectDto.getTeamId());
        projectRepository.save(project);
        return Response.status(Response.Status.CREATED).entity(toDto(project)).build();
    }

    private ProjectDto toDto(Project project) {
        ProjectDto dto = new ProjectDto();
        dto.setId(project.getId());
        dto.setName(project.getName());
        dto.setTeamId(project.getTeamId());
        return dto;
    }
}
