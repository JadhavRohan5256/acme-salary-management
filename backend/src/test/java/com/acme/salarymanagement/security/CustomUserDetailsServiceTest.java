package com.acme.salarymanagement.security;

import com.acme.salarymanagement.entity.User;
import com.acme.salarymanagement.repository.UserRepository;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {
    @Mock
    private UserRepository userRepository;
    private CustomUserDetailsService userDetailsService;

    @BeforeEach
    void beforeEach() {
        userDetailsService = new CustomUserDetailsService(userRepository);
    }

    @Test
    void loadUserByUsername_shouldReturnUserDetails_whenUserExists() {
        User user = new User();
        user.setId(1L);
        user.setUsername("admin");
        user.setPasswordHash("password123");
        user.setRole("ADMIN");

        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(user));

        UserDetails result = userDetailsService.loadUserByUsername("admin");

        assertNotNull(result);
        assertEquals("admin", result.getUsername());
        assertEquals("password123", result.getPassword());
        assertTrue(
        	result.getAuthorities()
        	.stream()
            .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"))
        );

        verify(userRepository).findByUsername("admin");
    }

    @Test
    void loadUserByUsername_shouldThrowException_whenUserDoesNotExist() {
        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername("unknown"));

        assertEquals("User not found: unknown", exception.getMessage());
        verify(userRepository).findByUsername("unknown");
    }

    @Test
    void loadUserByUsername_shouldCreateUserWithCorrectRole() {
        User user = new User();
        user.setId(2L);
        user.setUsername("employee");
        user.setPasswordHash("hashedPassword");
        user.setRole("USER");

        when(userRepository.findByUsername("employee")).thenReturn(Optional.of(user));

        UserDetails result = userDetailsService.loadUserByUsername("employee");

        assertEquals("employee", result.getUsername());
        assertEquals("hashedPassword", result.getPassword());

        assertTrue(
        	result.getAuthorities()
            .stream()
            .anyMatch(authority -> authority.getAuthority().equals("ROLE_USER"))
        );

        verify(userRepository).findByUsername("employee");
    }

    @Test
    void loadUserByUsername_shouldReturnEnabledUser() {
        User user = new User();
        user.setId(3L);
        user.setUsername("admin");
        user.setPasswordHash("password123");
        user.setRole("ADMIN");

        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(user));

        UserDetails result = userDetailsService.loadUserByUsername("admin");

        assertTrue(result.isEnabled());
        assertTrue(result.isAccountNonExpired());
        assertTrue(result.isAccountNonLocked());
        assertTrue(result.isCredentialsNonExpired());

        verify(userRepository).findByUsername("admin");
    }
}