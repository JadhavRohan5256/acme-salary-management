package com.acme.salarymanagement.repository;

import com.acme.salarymanagement.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    void findByUsername_shouldReturnUser_whenUsernameExists() {
        User user = new User();
        user.setUsername("testuser");
        user.setPasswordHash("password123");
        user.setRole("USER");

        userRepository.save(user);

        Optional<User> result = userRepository.findByUsername("testuser");

        assertTrue(result.isPresent());
        assertEquals("testuser", result.get().getUsername());
    }

    @Test
    void existsByUsername_shouldReturnTrue_whenUsernameExists() {
        User user = new User();
        user.setUsername("testuser");
        user.setPasswordHash("password123");
        user.setRole("USER");

        userRepository.save(user);

        boolean result = userRepository.existsByUsername("testuser");
        assertTrue(result);
    }


    @Test
    void findByUsername_shouldReturnEmpty_whenUsernameDoesNotExist() {
    	Optional<User> result = userRepository.findByUsername("unknown_user");
    	
    	assertTrue(result.isEmpty());
    }

    @Test
    void existsByUsername_shouldReturnFalse_whenUsernameDoesNotExist() {
        boolean result = userRepository.existsByUsername("unknown_user");

        assertFalse(result);
    }
}