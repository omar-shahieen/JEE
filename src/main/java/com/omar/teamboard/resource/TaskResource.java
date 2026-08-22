package com.omar.teamboard.resource;

import com.omar.teamboard.dto.TaskAssignmentDto;
import com.omar.teamboard.dto.TaskDto;
import com.omar.teamboard.dto.TaskStatusUpdateDto;
import com.omar.teamboard.model.Task;
import com.omar.teamboard.service.TaskService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.stream.Collectors;

@Path("/tasks")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TaskResource {

    @Inject
    TaskService taskService;

    @GET
    @Path("/project/{projectId}")
    public List<TaskDto> getProjectTasks(@PathParam("projectId") String projectId) {
        return taskService.getProjectTasks(projectId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @GET
    @Path("/{id}")
    public Response getTask(@PathParam("id") String id) {
        return taskService.getTask(id)
                .map(this::toDto)
                .map(dto -> Response.ok(dto).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @POST
    public Response createTask(@Valid TaskDto taskDto, @HeaderParam("X-User-Id") String requesterId) {
        if (requesterId == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("X-User-Id header is required").build();
        }
        Task created = taskService.createTask(taskDto.getTitle(), taskDto.getDescription(), taskDto.getProjectId(), taskDto.getAssignedUserId(), requesterId);
        return Response.status(Response.Status.CREATED).entity(toDto(created)).build();
    }

    @PATCH
    @Path("/{id}/status")
    public Response updateStatus(@PathParam("id") String id, @Valid TaskStatusUpdateDto statusUpdate, @HeaderParam("X-User-Id") String requesterId) {
        if (requesterId == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("X-User-Id header is required").build();
        }
        taskService.updateStatus(id, statusUpdate.getStatus(), requesterId);
        return Response.ok().build();
    }

    @PATCH
    @Path("/{id}/assign")
    public Response assignTask(@PathParam("id") String id, @Valid TaskAssignmentDto assignment, @HeaderParam("X-User-Id") String requesterId) {
        if (requesterId == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("X-User-Id header is required").build();
        }
        taskService.assignTask(id, assignment.getAssignedUserId(), requesterId);
        return Response.ok().build();
    }

    private TaskDto toDto(Task task) {
        TaskDto dto = new TaskDto();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setProjectId(task.getProjectId());
        dto.setAssignedUserId(task.getAssignedUserId());
        dto.setStatus(task.getStatus());
        return dto;
    }
}
