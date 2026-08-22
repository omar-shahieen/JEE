package com.omar.teamboard.dto;

import jakarta.validation.constraints.NotBlank;

public class TaskAssignmentDto {
    @NotBlank(message = "Assigned user ID cannot be blank")
    private String assignedUserId;

    public String getAssignedUserId() {
        return assignedUserId;
    }

    public void setAssignedUserId(String assignedUserId) {
        this.assignedUserId = assignedUserId;
    }
}
