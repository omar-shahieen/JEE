package com.omar.teamboard.repository;

import com.omar.teamboard.model.User;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

public class RepositoryTest {

    @Test
    public void testUserRepository() {
        UserRepository repo = new UserRepository();
        User u = new User("u100", "Test User");
        repo.save(u);

        Optional<User> found = repo.findById("u100");
        assertTrue(found.isPresent());
        assertEquals("Test User", found.get().getName());

        repo.delete("u100");
        assertFalse(repo.findById("u100").isPresent());
    }
}
