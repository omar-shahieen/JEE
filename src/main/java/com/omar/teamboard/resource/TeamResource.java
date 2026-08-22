package com.omar.teamboard.resource;

import com.omar.teamboard.dto.TeamDto;
import com.omar.teamboard.model.Team;
import com.omar.teamboard.service.TeamService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.stream.Collectors;

@Path("/teams")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TeamResource {

    @Inject
    TeamService teamService;

    @GET
    public List<TeamDto> getTeams() {
        return teamService.getAllTeams().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @GET
    @Path("/{id}")
    public Response getTeam(@PathParam("id") String id) {
        return teamService.getTeam(id)
                .map(this::toDto)
                .map(dto -> Response.ok(dto).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @POST
    public Response createTeam(@Valid TeamDto teamDto) {
        Team created = teamService.createTeam(teamDto.getName(), teamDto.getOwnerId());
        return Response.status(Response.Status.CREATED).entity(toDto(created)).build();
    }

    @POST
    @Path("/{id}/members")
    public Response addMember(@PathParam("id") String id, @QueryParam("memberId") String memberId, @HeaderParam("X-User-Id") String requesterId) {
        if (memberId == null || requesterId == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("memberId query param and X-User-Id header are required").build();
        }
        try {
            teamService.addMember(id, memberId, requesterId);
            return Response.ok().build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @DELETE
    @Path("/{id}/members/{memberId}")
    public Response removeMember(@PathParam("id") String id, @PathParam("memberId") String memberId, @HeaderParam("X-User-Id") String requesterId) {
        if (requesterId == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("X-User-Id header is required").build();
        }
        try {
            teamService.removeMember(id, memberId, requesterId);
            return Response.ok().build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    private TeamDto toDto(Team team) {
        TeamDto dto = new TeamDto();
        dto.setId(team.getId());
        dto.setName(team.getName());
        dto.setOwnerId(team.getOwnerId());
        dto.setMemberIds(team.getMemberIds());
        return dto;
    }
}
