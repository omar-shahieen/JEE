package com.omar.teamboard.repository;

import com.omar.teamboard.model.Project;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@ApplicationScoped
public class ProjectRepository {
    private final Map<String, Project> projects = new ConcurrentHashMap<>();

    public void save(Project project) {
        projects.put(project.getId(), project);
    }

    public Optional<Project> findById(String id) {
        return Optional.ofNullable(projects.get(id));
    }

    public List<Project> findAll() {
        return new ArrayList<>(projects.values());
    }

    public List<Project> findByTeamId(String teamId) {
        return projects.values().stream()
                .filter(p -> p.getTeamId().equals(teamId))
                .collect(Collectors.toList());
    }

    public void delete(String id) {
        projects.remove(id);
    }
}
