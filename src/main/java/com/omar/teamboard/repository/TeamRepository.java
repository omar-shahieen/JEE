package com.omar.teamboard.repository;

import com.omar.teamboard.model.Team;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
public class TeamRepository {
    private final Map<String, Team> teams = new ConcurrentHashMap<>();

    public void save(Team team) {
        teams.put(team.getId(), team);
    }

    public Optional<Team> findById(String id) {
        return Optional.ofNullable(teams.get(id));
    }

    public List<Team> findAll() {
        return new ArrayList<>(teams.values());
    }

    public void delete(String id) {
        teams.remove(id);
    }
}
