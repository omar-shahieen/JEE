package com.omar.teamboard.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.Set;

public class TeamDto {
    private String id;
    
    @NotBlank(message = "Team name cannot be blank")
    private String name;
    
    @NotBlank(message = "Owner ID cannot be blank")
    private String ownerId;
    
    private Set<String> memberIds;

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

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public Set<String> getMemberIds() {
        return memberIds;
    }

    public void setMemberIds(Set<String> memberIds) {
        this.memberIds = memberIds;
    }
}
