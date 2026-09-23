package com.acme.salarymanagement.dto.auth;

public record LoginResponse(
    String accessToken,
    String tokenType,
    long expiresIn,
    UserInfo user
) {

    public record UserInfo(
        Long id, 
        String username, 
        String role
    ) {}
}