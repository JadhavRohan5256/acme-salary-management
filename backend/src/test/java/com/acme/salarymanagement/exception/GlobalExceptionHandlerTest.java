package com.acme.salarymanagement.exception;

import jakarta.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GlobalExceptionHandlerTest {
    private GlobalExceptionHandler handler;
    private HttpServletRequest request;

    @BeforeEach
    void beforeEach() {
        handler = new GlobalExceptionHandler();
        request = mock(HttpServletRequest.class);

        when(request.getRequestURI()).thenReturn("/api/employees/123");
    }

    @Test
    void handleBadCredentials_shouldReturn401() {
        BadCredentialsException exception = new BadCredentialsException("Invalid credentials");
        ResponseEntity<ErrorResponse> response = handler.handleBadCredentials(exception, request);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(401, response.getBody().status());
        assertEquals("UNAUTHORIZED", response.getBody().error());
        assertEquals("Invalid username or password", response.getBody().message());
        assertEquals("/api/employees/123", response.getBody().path());
        assertNull(response.getBody().fieldErrors());
    }

    @Test
    void handleNotFound_shouldReturn404() {
        ResourceNotFoundException exception = new ResourceNotFoundException("Employee not found");
        ResponseEntity<ErrorResponse> response = handler.handleNotFound(exception, request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(404, response.getBody().status());
        assertEquals("NOT_FOUND", response.getBody().error());
        assertEquals("Employee not found", response.getBody().message());
        assertEquals("/api/employees/123", response.getBody().path());
        assertNull(response.getBody().fieldErrors());
    }

    @Test
    void handleBadRequest_shouldReturn400() {
        BadRequestException exception = new BadRequestException("Invalid sort field: abc");
        ResponseEntity<ErrorResponse> response = handler.handleBadRequest(exception, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(400, response.getBody().status());
        assertEquals("BAD_REQUEST", response.getBody().error());
        assertEquals("Invalid sort field: abc", response.getBody().message());
        assertEquals("/api/employees/123", response.getBody().path());
        assertNull(response.getBody().fieldErrors());
    }

    @Test
    void handleGenericException_shouldReturn500() {
        Exception exception = new RuntimeException("Something went wrong");
        ResponseEntity<ErrorResponse> response = handler.handleGenericException(exception, request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

        assertNotNull(response.getBody());
        assertEquals(500, response.getBody().status());
        assertEquals("INTERNAL_SERVER_ERROR", response.getBody().error());
        assertEquals("An unexpected error occurred", response.getBody().message());
        assertEquals("/api/employees/123", response.getBody().path());
        assertNull(response.getBody().fieldErrors());
    }
}