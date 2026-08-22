package com.omar.teamboard.service;

import com.omar.teamboard.model.Project;
import com.omar.teamboard.model.Task;
import com.omar.teamboard.model.TaskStatus;
import com.omar.teamboard.model.Team;
import com.omar.teamboard.repository.ProjectRepository;
import com.omar.teamboard.repository.TaskRepository;
import com.omar.teamboard.repository.TeamRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class TaskService {

    @Inject
    TaskRepository taskRepository;

    @Inject
    ProjectRepository projectRepository;

    @Inject
    TeamRepository teamRepository;

    public List<Task> getProjectTasks(String projectId) {
        return taskRepository.findByProjectId(projectId);
    }

    public Optional<Task> getTask(String id) {
        return taskRepository.findById(id);
    }

    public Task createTask(String title, String description, String projectId, String assignedUserId, String requesterId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));
        Team team = teamRepository.findById(project.getTeamId())
                .orElseThrow(() -> new IllegalArgumentException("Team not found"));

        if (!team.getOwnerId().equals(requesterId) && !team.getMemberIds().contains(requesterId)) {
            throw new IllegalArgumentException("Only team members can create tasks in this project");
        }

        if (assignedUserId != null && !team.getMemberIds().contains(assignedUserId) && !team.getOwnerId().equals(assignedUserId)) {
            throw new IllegalArgumentException("Assigned user must be a team member or owner");
        }

        Task task = new Task(UUID.randomUUID().toString(), title, description, projectId, assignedUserId, TaskStatus.ASSIGNED);
        taskRepository.save(task);
        return task;
    }

    public void assignTask(String taskId, String assignedUserId, String requesterId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));
        Project project = projectRepository.findById(task.getProjectId())
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));
        Team team = teamRepository.findById(project.getTeamId())
                .orElseThrow(() -> new IllegalArgumentException("Team not found"));

        if (!team.getOwnerId().equals(requesterId) && !team.getMemberIds().contains(requesterId)) {
            throw new IllegalArgumentException("Only team members can assign tasks");
        }

        if (!team.getMemberIds().contains(assignedUserId) && !team.getOwnerId().equals(assignedUserId)) {
            throw new IllegalArgumentException("Assigned user must be a team member or owner");
        }

        task.setAssignedUserId(assignedUserId);
        taskRepository.save(task);
    }

    public void updateStatus(String taskId, TaskStatus newStatus, String requesterId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));
        Project project = projectRepository.findById(task.getProjectId())
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));
        Team team = teamRepository.findById(project.getTeamId())
                .orElseThrow(() -> new IllegalArgumentException("Team not found"));

        if (!team.getOwnerId().equals(requesterId) && !requesterId.equals(task.getAssignedUserId())) {
            throw new IllegalArgumentException("Only the team owner or the assigned user can change the task status");
        }

        if (task.getStatus() == TaskStatus.ASSIGNED && newStatus == TaskStatus.COMPLETED) {
            throw new IllegalArgumentException("Cannot skip IN_PROGRESS status");
        }
        
        if (task.getStatus() == TaskStatus.COMPLETED && newStatus != TaskStatus.COMPLETED) {
            throw new IllegalArgumentException("Cannot change status of a completed task");
        }

        task.setStatus(newStatus);
        taskRepository.save(task);
    }
}
