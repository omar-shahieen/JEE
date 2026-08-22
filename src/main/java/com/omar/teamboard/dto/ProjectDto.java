package com.omar.teamboard.dto;

import jakarta.validation.constraints.NotBlank;

public class ProjectDto {
    private String id;
    
    @NotBlank(message = "Project name cannot be blank")
    private String name;
    
    @NotBlank(message = "Team ID cannot be blank")
    private String teamId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }
}
