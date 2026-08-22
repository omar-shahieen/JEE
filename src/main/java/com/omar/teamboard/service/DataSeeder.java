package com.omar.teamboard.service;

import com.omar.teamboard.model.Project;
import com.omar.teamboard.model.Task;
import com.omar.teamboard.model.TaskStatus;
import com.omar.teamboard.model.Team;
import com.omar.teamboard.model.User;
import com.omar.teamboard.repository.ProjectRepository;
import com.omar.teamboard.repository.TaskRepository;
import com.omar.teamboard.repository.TeamRepository;
import com.omar.teamboard.repository.UserRepository;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;

import java.util.Set;
import java.util.UUID;

@ApplicationScoped
public class DataSeeder {

    @Inject
    UserRepository userRepository;

    @Inject
    TeamRepository teamRepository;

    @Inject
    ProjectRepository projectRepository;

    @Inject
    TaskRepository taskRepository;

    void onStart(@Observes StartupEvent ev) {
        // Create users
        User alice = new User("u1", "Alice");
        User bob = new User("u2", "Bob");
        User charlie = new User("u3", "Charlie");
        
        userRepository.save(alice);
        userRepository.save(bob);
        userRepository.save(charlie);

        // Create team
        Team teamAlpha = new Team("t1", "Team Alpha", alice.getId());
        teamAlpha.getMemberIds().addAll(Set.of(alice.getId(), bob.getId()));
        teamRepository.save(teamAlpha);

        // Create project
        Project projectX = new Project("p1", "Project X", teamAlpha.getId());
        projectRepository.save(projectX);

        // Create tasks
        Task task1 = new Task(UUID.randomUUID().toString(), "Design DB", "Design the schema", projectX.getId(), bob.getId(), TaskStatus.IN_PROGRESS);
        Task task2 = new Task(UUID.randomUUID().toString(), "Setup UI", "Create Qute templates", projectX.getId(), alice.getId(), TaskStatus.ASSIGNED);
        
        taskRepository.save(task1);
        taskRepository.save(task2);
    }
}
