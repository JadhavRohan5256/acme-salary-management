package com.acme.salarymanagement.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.SecretKey;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {
    private JwtService jwtService;
    private static final String SECRET = "my-super-secret-key-that-is-at-least-32-characters-long";
    private static final long EXPIRATION_MS = 3600000L;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService(SECRET, EXPIRATION_MS);
    }

    @Test
    void generateToken_shouldCreateValidToken() {
        String token = jwtService.generateToken("admin", "ADMIN");

        assertNotNull(token);
        assertFalse(token.isBlank());
    }

    @Test
    void generateToken_shouldContainUsername() {
        String token = jwtService.generateToken("admin", "ADMIN");
        String username = jwtService.extractUsername(token);

        assertEquals("admin", username);
    }

    @Test
    void generateToken_shouldContainRole() {

        String token = jwtService.generateToken("admin", "ADMIN");

        SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

        Claims claims = Jwts.parser()
        		.verifyWith(key)
        		.build()
        		.parseSignedClaims(token)
        		.getPayload();

        assertEquals("ADMIN", claims.get("role"));
    }

    @Test
    void generateToken_shouldContainIssuedAtAndExpiration() {
        String token = jwtService.generateToken("admin", "ADMIN");

        SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

        Claims claims = Jwts.parser()
        		.verifyWith(key)
        		.build()
        		.parseSignedClaims(token)
        		.getPayload();

        assertNotNull(claims.getIssuedAt());
        assertNotNull(claims.getExpiration());
        assertTrue(claims.getExpiration().after(claims.getIssuedAt()));
    }

    @Test
    void extractUsername_shouldReturnUsernameFromToken() {
        String token =	jwtService.generateToken("testuser", "USER");
        String username = jwtService.extractUsername(token);

        assertEquals("testuser", username);
    }

    @Test
    void isTokenValid_shouldReturnTrue_whenTokenIsValid() {
        String token = jwtService.generateToken("admin", "ADMIN");

        UserDetails userDetails = User.withUsername("admin")
        		.password("password")
        		.roles("ADMIN")
        		.build();

        boolean result = jwtService.isTokenValid(token, userDetails);

        assertTrue(result);
    }

    @Test
    void isTokenValid_shouldReturnFalse_whenUsernameDoesNotMatch() {
        String token = jwtService.generateToken("admin", "ADMIN");

        UserDetails userDetails = User.withUsername("user")
        		.password("password")
        		.roles("USER")
        		.build();

        boolean result = jwtService.isTokenValid(token, userDetails);

        assertFalse(result);
    }

    @Test
    void isTokenValid_shouldThrowException_whenTokenIsExpired() {

        JwtService expiredJwtService = new JwtService(SECRET, -1000L);
        String token = expiredJwtService.generateToken("admin", "ADMIN");

        UserDetails userDetails = User.withUsername("admin")
        		.password("password")
        		.roles("ADMIN")
        		.build();

        assertThrows(io.jsonwebtoken.ExpiredJwtException.class, () -> expiredJwtService.isTokenValid(token, userDetails));
    }

    @Test
    void extractUsername_shouldThrowException_whenTokenIsInvalid() {
        String invalidToken = "invalid.jwt.token";

        assertThrows(Exception.class, () -> jwtService.extractUsername(invalidToken));
    }

    @Test
    void extractUsername_shouldThrowException_whenTokenIsSignedWithDifferentKey() {
        String differentSecret = "another-super-secret-key-that-is-at-least-32-chars";

        SecretKey differentKey = Keys.hmacShaKeyFor(differentSecret.getBytes(StandardCharsets.UTF_8));

        String token = Jwts.builder()
        		.subject("admin")
        		.claim("role", "ADMIN")
        		.issuedAt(new java.util.Date())
        		.expiration(new java.util.Date(System.currentTimeMillis() + 3600000L))
        		.signWith(differentKey)
        		.compact();

        assertThrows(Exception.class, () -> jwtService.extractUsername(token));
    }
}