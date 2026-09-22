package com.acme.salarymanagement.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.authentication.BadCredentialsException;

import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomAuthenticationEntryPointTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;

    private CustomAuthenticationEntryPoint authenticationEntryPoint;
    private StringWriter stringWriter;

    @BeforeEach
    void beforeEach() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        authenticationEntryPoint = new CustomAuthenticationEntryPoint(objectMapper);
        stringWriter = new StringWriter();

        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));
        when(request.getRequestURI()).thenReturn("/api/employees");
    }

    @Test
    void commence_shouldReturn401Unauthorized() throws Exception {
        BadCredentialsException exception = new BadCredentialsException("Invalid credentials");

        authenticationEntryPoint.commence(
                request,
                response,
                exception
        );

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(response).setContentType("application/json");

        String responseBody = stringWriter.toString();

        assertTrue(responseBody.contains("\"status\":401"));
        assertTrue(responseBody.contains("\"error\":\"UNAUTHORIZED\""));
        assertTrue(responseBody.contains("\"message\":\"Authentication required or token is invalid\""));
        assertTrue(responseBody.contains("\"path\":\"/api/employees\""));
    }

    @Test
    void commence_shouldSetCorrectContentType() throws Exception {
        authenticationEntryPoint.commence(
                request,
                response,
                new BadCredentialsException("Invalid credentials")
        );

        verify(response).setContentType("application/json");
    }

    @Test
    void commence_shouldIncludeRequestPath() throws Exception {
        when(request.getRequestURI()).thenReturn("/api/employees/1");

        authenticationEntryPoint.commence(
                request,
                response,
                new BadCredentialsException("Invalid credentials")
        );

        String responseBody = stringWriter.toString();

        assertTrue(responseBody.contains("\"path\":\"/api/employees/1\""));
    }
}