package com.omar.teamboard.resource;

import com.omar.teamboard.model.Project;
import com.omar.teamboard.model.Team;
import com.omar.teamboard.service.TaskService;
import com.omar.teamboard.service.TeamService;
import com.omar.teamboard.repository.ProjectRepository;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.net.URI;

@Path("/ui")
@Produces(MediaType.TEXT_HTML)
public class UiResource {

    @Inject
    Template teams;

    @Inject
    Template projects;
    
    @Inject
    Template board;

    @Inject
    TeamService teamService;

    @Inject
    ProjectRepository projectRepository;

    @Inject
    TaskService taskService;

    @GET
    public Response index() {
        return Response.seeOther(URI.create("/ui/teams")).build();
    }

    @GET
    @Path("/teams")
    public TemplateInstance showTeams() {
        return teams.data("teams", teamService.getAllTeams());
    }

    @GET
    @Path("/teams/{teamId}/projects")
    public TemplateInstance showProjects(@PathParam("teamId") String teamId) {
        Team team = teamService.getTeam(teamId).orElse(null);
        return projects.data("projects", projectRepository.findByTeamId(teamId))
                       .data("teamId", teamId)
                       .data("team", team);
    }
    
    @GET
    @Path("/projects/{projectId}/board")
    public TemplateInstance showBoard(@PathParam("projectId") String projectId, 
                                      @QueryParam("teamId") String teamId,
                                      @QueryParam("requesterId") String requesterId) {
        Project project = projectRepository.findById(projectId).orElse(null);
        // We require requesterId for the HTMX status update calls in the board
        return board.data("project", project)
                    .data("tasks", taskService.getProjectTasks(projectId))
                    .data("requesterId", requesterId != null ? requesterId : "u1"); // fallback to u1 (Alice)
    }
}
