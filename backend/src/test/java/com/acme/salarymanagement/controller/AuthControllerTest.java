
package com.acme.salarymanagement.controller;

import com.acme.salarymanagement.dto.auth.LoginRequest;
import com.acme.salarymanagement.dto.auth.LoginResponse;
import com.acme.salarymanagement.service.AuthService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {
    @Mock
    private AuthService authService;
    
    @InjectMocks
    private AuthController authController;


    @Test
    void login_shouldReturnSuccessfulResponse() {
        LoginRequest request = new LoginRequest("admin", "password123");

        LoginResponse.UserInfo userInfo = new LoginResponse.UserInfo(
        	1L,
        	"admin",
        	"ADMIN"
        );

        LoginResponse expectedResponse = new LoginResponse(
        	"jwt-token",
        	"Bearer",
        	3600L,
        	userInfo
        );

        when(this.authService.login(request)).thenReturn(expectedResponse);

        ResponseEntity<LoginResponse> actualResponse = this.authController.login(request);

        assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
        assertSame(expectedResponse, actualResponse.getBody());
        verify(this.authService).login(request);
    }
}
