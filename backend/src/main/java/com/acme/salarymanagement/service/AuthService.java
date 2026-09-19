package com.acme.salarymanagement.service;

import com.acme.salarymanagement.dto.auth.LoginRequest;
import com.acme.salarymanagement.dto.auth.LoginResponse;
import com.acme.salarymanagement.entity.User;
import com.acme.salarymanagement.repository.UserRepository;
import com.acme.salarymanagement.security.JwtService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
	private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final long expirationMs;

    public AuthService(
            AuthenticationManager authenticationManager,
            UserRepository userRepository,
            JwtService jwtService,
            @Value("${security.jwt.expiration-ms}") long expirationMs
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.expirationMs = expirationMs;
    }

    public LoginResponse login(LoginRequest request) {
    	UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(request.username(), request.password());
        User user = userRepository.findByUsername(request.username()).orElseThrow();
        String token = jwtService.generateToken(user.getUsername(), user.getRole());

        authenticationManager.authenticate(authToken);
        return new LoginResponse(
                token,
                "Bearer",
                expirationMs / 1000,
                new LoginResponse.UserInfo(
                        user.getId(),
                        user.getUsername(),
                        user.getRole()
                )
        );
    }
}