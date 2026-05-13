package com.jwt.project.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtUtilsTest {

    private JwtUtils jwtUtils;

    @BeforeEach
    void setUp() {
        jwtUtils = new JwtUtils();
        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", "mySecretKey1234567890123456789012345678901234567890");
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", 900000L);
        ReflectionTestUtils.setField(jwtUtils, "jwtRefreshExpirationMs", 604800000L);
    }

    @Test
    void generateAccessToken_Success() {
        // Arrange
        var authentication = mock(org.springframework.security.core.Authentication.class);
        var userPrincipal = mock(UserPrincipal.class);
        when(authentication.getPrincipal()).thenReturn(userPrincipal);
        when(userPrincipal.getUsername()).thenReturn("testuser");

        // Act
        String token = jwtUtils.generateAccessToken(authentication);

        // Assert
        assertNotNull(token);
        assertTrue(token.length() > 0);
        assertTrue(jwtUtils.validateJwtToken(token));
        assertEquals("testuser", jwtUtils.getUsernameFromJwtToken(token));
    }

    @Test
    void generateRefreshToken_Success() {
        // Arrange
        var authentication = mock(org.springframework.security.core.Authentication.class);
        var userPrincipal = mock(UserPrincipal.class);
        when(authentication.getPrincipal()).thenReturn(userPrincipal);
        when(userPrincipal.getUsername()).thenReturn("testuser");

        // Act
        String token = jwtUtils.generateRefreshToken(authentication);

        // Assert
        assertNotNull(token);
        assertTrue(token.length() > 0);
        assertTrue(jwtUtils.validateJwtToken(token));
        assertEquals("testuser", jwtUtils.getUsernameFromJwtToken(token));
    }

    @Test
    void validateJwtToken_ValidToken_ReturnsTrue() {
        // Arrange
        var authentication = mock(org.springframework.security.core.Authentication.class);
        var userPrincipal = mock(UserPrincipal.class);
        when(authentication.getPrincipal()).thenReturn(userPrincipal);
        when(userPrincipal.getUsername()).thenReturn("testuser");
        String token = jwtUtils.generateAccessToken(authentication);

        // Act
        boolean isValid = jwtUtils.validateJwtToken(token);

        // Assert
        assertTrue(isValid);
    }

    @Test
    void validateJwtToken_InvalidToken_ReturnsFalse() {
        // Act
        boolean isValid = jwtUtils.validateJwtToken("invalid.token.here");

        // Assert
        assertFalse(isValid);
    }

    @Test
    void validateJwtToken_NullToken_ReturnsFalse() {
        // Act
        boolean isValid = jwtUtils.validateJwtToken(null);

        // Assert
        assertFalse(isValid);
    }

    @Test
    void getUsernameFromJwtToken_Success() {
        // Arrange
        var authentication = mock(org.springframework.security.core.Authentication.class);
        var userPrincipal = mock(UserPrincipal.class);
        when(authentication.getPrincipal()).thenReturn(userPrincipal);
        when(userPrincipal.getUsername()).thenReturn("testuser");
        String token = jwtUtils.generateAccessToken(authentication);

        // Act
        String username = jwtUtils.getUsernameFromJwtToken(token);

        // Assert
        assertEquals("testuser", username);
    }
}
