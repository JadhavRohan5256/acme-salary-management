package com.acme.salarymanagement.service;

import com.acme.salarymanagement.dto.auth.LoginRequest;
import com.acme.salarymanagement.dto.auth.LoginResponse;
import com.acme.salarymanagement.entity.User;
import com.acme.salarymanagement.exception.ResourceNotFoundException;
import com.acme.salarymanagement.repository.UserRepository;
import com.acme.salarymanagement.security.JwtService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private UserRepository userRepository;
    @Mock
    private JwtService jwtService;
    private AuthService authService;
    private User user;
    private LoginRequest loginRequest;

    @BeforeEach
    void beforeEach() {
        authService = new AuthService(
                authenticationManager,
                userRepository,
                jwtService,
                3600000L
        );

        user = new User();
        user.setId(1L);
        user.setUsername("admin");
        user.setPasswordHash("password123");
        user.setRole("ADMIN");

        loginRequest = new LoginRequest("admin", "password123");
    }

    // @Test
    // void login_shouldReturnLoginResponse_whenCredentialsAreValid() {
    //     when(userRepository.findByUsername("admin")).thenReturn(Optional.of(user));

    //     when(jwtService.generateToken("admin", "ADMIN")).thenReturn("jwt-token");

    //     LoginResponse result = authService.login(loginRequest);

    //     assertNotNull(result);
    //     assertEquals("jwt-token", result.accessToken());
    //     assertEquals("Bearer", result.tokenType());
    //     assertEquals(3600L, result.expiresIn());

    //     assertNotNull(result.user());
    //     assertEquals(1L, result.user().id());
    //     assertEquals("admin", result.user().username());
    //     assertEquals("ADMIN", result.user().role());

    //     verify(userRepository).findByUsername("admin");
    //     verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    //     verify(jwtService).generateToken("admin", "ADMIN");
    // }

    @Test
    void login_shouldReturnLoginResponse_whenCredentialsAreValid() {
        when(authenticationManager.authenticate(
            any(UsernamePasswordAuthenticationToken.class)
        )).thenReturn(null);

        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(user));

        when(jwtService.generateToken("admin", "ADMIN")).thenReturn("jwt-token");

        LoginResponse result = authService.login(loginRequest);

        assertNotNull(result);
        assertEquals("jwt-token", result.accessToken());
        assertEquals("Bearer", result.tokenType());
        assertEquals(3600L, result.expiresIn());

        assertNotNull(result.user());
        assertEquals(1L, result.user().id());
        assertEquals("admin", result.user().username());
        assertEquals("ADMIN", result.user().role());

        verify(authenticationManager).authenticate(
            any(UsernamePasswordAuthenticationToken.class)
        );

        verify(userRepository).findByUsername("admin");
        verify(jwtService).generateToken("admin", "ADMIN");
    }

    @Test
    void login_shouldThrowException_whenUserDoesNotExist() {
        when(authenticationManager.authenticate(
            any(UsernamePasswordAuthenticationToken.class)
        )).thenReturn(null);

        when(userRepository.findByUsername("admin")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> authService.login(loginRequest));

        verify(authenticationManager).authenticate(
            any(UsernamePasswordAuthenticationToken.class)
        );

        verify(userRepository).findByUsername("admin");
        verifyNoInteractions(jwtService);
    }

    @Test
    void login_shouldAuthenticateWithProvidedCredentials() {
        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(user));
        when(jwtService.generateToken("admin", "ADMIN")).thenReturn("jwt-token");

        authService.login(loginRequest);

        verify(authenticationManager).authenticate(
                argThat(authentication -> {

                    UsernamePasswordAuthenticationToken token =(UsernamePasswordAuthenticationToken) authentication;
                    return token.getPrincipal().equals("admin")
                            && token.getCredentials().equals("password123");
                })
        );
    }

    @Test
    void login_shouldGenerateTokenUsingUserDetails() {
        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(user));
        when(jwtService.generateToken("admin", "ADMIN")).thenReturn("jwt-token");

        authService.login(loginRequest);

        verify(jwtService).generateToken("admin", "ADMIN");
    }
}