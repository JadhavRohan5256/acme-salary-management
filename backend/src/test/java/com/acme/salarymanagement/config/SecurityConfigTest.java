package com.acme.salarymanagement.config;

import com.acme.salarymanagement.security.JwtAuthenticationFilter;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SecurityConfigTest {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private SecurityFilterChain securityFilterChain;
    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;


    @Test
    void shouldCreatePasswordEncoder() {

        assertNotNull(passwordEncoder);
        assertInstanceOf(BCryptPasswordEncoder.class, passwordEncoder);
    }


    @Test
    void shouldEncodeAndMatchPassword() {
        String rawPassword = "password123";
        String encodedPassword = passwordEncoder.encode(rawPassword);

        assertNotNull(encodedPassword);
        assertNotEquals(rawPassword, encodedPassword);
        assertTrue(passwordEncoder.matches(rawPassword, encodedPassword));
    }


    @Test
    void shouldCreateSecurityFilterChain() {
        assertNotNull(securityFilterChain);
    }
}