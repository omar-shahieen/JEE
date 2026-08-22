package com.omar.teamboard.service;

import com.omar.teamboard.model.Team;
import com.omar.teamboard.repository.TeamRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class TeamService {

    @Inject
    TeamRepository teamRepository;

    @ConfigProperty(name = "teamboard.max-team-size", defaultValue = "10")
    int maxTeamSize;

    public List<Team> getAllTeams() {
        return teamRepository.findAll();
    }

    public Optional<Team> getTeam(String id) {
        return teamRepository.findById(id);
    }

    public Team createTeam(String name, String ownerId) {
        Team team = new Team(UUID.randomUUID().toString(), name, ownerId);
        team.getMemberIds().add(ownerId); // Owner is naturally a member
        teamRepository.save(team);
        return team;
    }

    public void addMember(String teamId, String memberId, String requesterId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Team not found"));

        if (!team.getOwnerId().equals(requesterId)) {
            throw new IllegalArgumentException("Only the team owner can add members");
        }

        if (team.getMemberIds().size() >= maxTeamSize) {
            throw new IllegalArgumentException("Team has reached the maximum size of " + maxTeamSize);
        }

        team.getMemberIds().add(memberId);
        teamRepository.save(team);
    }
    
    public void removeMember(String teamId, String memberId, String requesterId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Team not found"));

        if (!team.getOwnerId().equals(requesterId)) {
            throw new IllegalArgumentException("Only the team owner can remove members");
        }
        
        if (team.getOwnerId().equals(memberId)) {
            throw new IllegalArgumentException("Cannot remove the team owner");
        }

        team.getMemberIds().remove(memberId);
        teamRepository.save(team);
    }
}
