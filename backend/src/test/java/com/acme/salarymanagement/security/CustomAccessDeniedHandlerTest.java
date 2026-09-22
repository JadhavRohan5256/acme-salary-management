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
import org.springframework.security.access.AccessDeniedException;

import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomAccessDeniedHandlerTest {
    @Mock
    private HttpServletRequest request;
    @Mock

    private HttpServletResponse response;
    private CustomAccessDeniedHandler accessDeniedHandler;
    private StringWriter stringWriter;

    @BeforeEach
    void beforeEach() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        accessDeniedHandler = new CustomAccessDeniedHandler(objectMapper);
        stringWriter = new StringWriter();

        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));
        when(request.getRequestURI()).thenReturn("/api/employees/1/salary");
    }

    @Test
    void handle_shouldReturn403Forbidden() throws Exception {
        AccessDeniedException exception = new AccessDeniedException("Access denied");

        accessDeniedHandler.handle(
            request,
            response,
            exception
        );

        verify(response).setStatus(HttpServletResponse.SC_FORBIDDEN);
        verify(response).setContentType("application/json");

        String responseBody = stringWriter.toString();

        assertTrue(responseBody.contains("\"status\":403"));
        assertTrue(responseBody.contains("\"error\":\"FORBIDDEN\""));
        assertTrue(responseBody.contains("\"message\":\"You do not have permission to perform this operation\""));
        assertTrue(responseBody.contains("\"path\":\"/api/employees/1/salary\""));
    }

    @Test
    void handle_shouldSetCorrectContentType() throws Exception {
        accessDeniedHandler.handle(
            request,
            response,
            new AccessDeniedException("Access denied")
        );

        verify(response).setContentType("application/json");
    }

    @Test
    void handle_shouldIncludeRequestPath() throws Exception {
        when(request.getRequestURI()).thenReturn("/api/employees");

        accessDeniedHandler.handle(
            request,
            response,
            new AccessDeniedException("Access denied")
        );

        String responseBody = stringWriter.toString();

        assertTrue(responseBody.contains("\"path\":\"/api/employees\""));
    }
}