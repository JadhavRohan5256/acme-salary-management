package com.acme.salarymanagement.seeder;

import com.acme.salarymanagement.entity.User;
import com.acme.salarymanagement.repository.UserRepository;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

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
    void shouldSkipUserCreationWhenUsersAlreadyExist() {
        when(userRepository.existsByUsername("hrmanager")).thenReturn(true);
        when(userRepository.existsByUsername("acmemanager")).thenReturn(true);

        seeder.run();

        verify(userRepository).existsByUsername("hrmanager");
        verify(userRepository).existsByUsername("acmemanager");

        verify(userRepository, never()).save(any(User.class));
        verify(passwordEncoder, never()).encode(anyString());
    }

    @Test
    void shouldCreateBothUsersWhenUsersDoNotExist() {
        when(userRepository.existsByUsername("hrmanager")).thenReturn(false);
        when(userRepository.existsByUsername("acmemanager")).thenReturn(false);
        when(passwordEncoder.encode("Admin@123")).thenReturn("encoded-admin-password");
        when(passwordEncoder.encode("Admin@12345")).thenReturn("encoded-acme-password");

        seeder.run();

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        verify(userRepository, times(2)).save(userCaptor.capture());
        var savedUsers = userCaptor.getAllValues();
        assertEquals(2, savedUsers.size());
        User hrManager = savedUsers.get(0);

        assertEquals("hrmanager", hrManager.getUsername());
        assertEquals(
            "encoded-admin-password",
            hrManager.getPasswordHash()
        );
        assertEquals("HR_MANAGER", hrManager.getRole());

        User acmeManager = savedUsers.get(1);

        assertEquals("acmemanager", acmeManager.getUsername());
        assertEquals(
            "encoded-acme-password",
            acmeManager.getPasswordHash()
        );
        assertEquals("HR_MANAGER", acmeManager.getRole());
    }

    @Test
    void shouldCreateAcmeManagerWhenUserDoesNotExist() {
        when(userRepository.existsByUsername("hrmanager")).thenReturn(true);
        when(userRepository.existsByUsername("acmemanager")).thenReturn(false);
        when(passwordEncoder.encode("Admin@12345")).thenReturn("encoded-acme-password");

        seeder.run();

        verify(userRepository).existsByUsername("hrmanager");
        verify(userRepository).existsByUsername("acmemanager");

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();

        assertEquals("acmemanager", savedUser.getUsername());
        assertEquals(
            "encoded-acme-password",
            savedUser.getPasswordHash()
        );
        assertEquals("HR_MANAGER", savedUser.getRole());
    }

    @Test
    void shouldEncodePasswordsBeforeSavingUsers() {
        when(userRepository.existsByUsername("hrmanager")).thenReturn(false);
        when(userRepository.existsByUsername("acmemanager")).thenReturn(false);
        when(passwordEncoder.encode("Admin@123")).thenReturn("encoded-admin-password");
        when(passwordEncoder.encode("Admin@12345")).thenReturn("encoded-acme-password");

        seeder.run();

        InOrder inOrder = inOrder(passwordEncoder, userRepository);

        inOrder.verify(passwordEncoder).encode("Admin@123");
        inOrder.verify(userRepository).save(any(User.class));
        inOrder.verify(passwordEncoder).encode("Admin@12345");
        inOrder.verify(userRepository).save(any(User.class));
    }
}