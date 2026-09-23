package com.acme.salarymanagement.seeder;

import com.acme.salarymanagement.entity.User;
import com.acme.salarymanagement.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserDataSeederTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    private UserDataSeeder seeder;

    @BeforeEach
    void beforeEach() {
        this.seeder = new UserDataSeeder(userRepository, passwordEncoder);
    }

    @Test
    void shouldSkipUserCreationWhenHrManagerAlreadyExists() {
        when(userRepository.existsByUsername("hrmanager")).thenReturn(true);

        this.seeder.run();

        verify(this.userRepository).existsByUsername("hrmanager");
        verify(this.userRepository, never()).save(any(User.class));
        verify(this.passwordEncoder, never()).encode(anyString());
    }

    @Test
    void shouldCreateHrManagerWhenUserDoesNotExist() {
        when(this.userRepository.existsByUsername("hrmanager")).thenReturn(false);
        when(this.passwordEncoder.encode("Admin@123")).thenReturn("encoded-password");

        this.seeder.run();

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        verify(userRepository).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();

        assertEquals("hrmanager", savedUser.getUsername());
        assertEquals("encoded-password", savedUser.getPasswordHash());
        assertEquals("HR_MANAGER", savedUser.getRole());
    }

    @Test
    void shouldEncodePasswordBeforeSavingUser() {
        when(this.userRepository.existsByUsername("hrmanager")).thenReturn(false);
        when(this.passwordEncoder.encode("Admin@123")).thenReturn("encoded-password");

        this.seeder.run();

        InOrder inOrder = inOrder(passwordEncoder, userRepository);

        inOrder.verify(passwordEncoder).encode("Admin@123");
        inOrder.verify(userRepository).save(any(User.class));
    }
}