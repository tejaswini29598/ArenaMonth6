package com.enterprise.auth.service;

import com.enterprise.auth.dto.*;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Authentication Service - JWT Token Generation and Validation
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    @Value("${app.jwt.secret:your-super-secret-jwt-key-min-32-characters-long-change-me}")
    private String jwtSecret;

    @Value("${app.jwt.expiration:86400000}")
    private long jwtExpiration; // 24 hours

    @Value("${app.jwt.refresh-expiration:604800000}")
    private long refreshExpiration; // 7 days

    private final PasswordEncoder passwordEncoder;

    /**
     * Authenticate user and generate JWT token
     */
    public LoginResponse login(LoginRequest request) {
        log.info("Login attempt for user: {}", request.getEmail());

        // TODO: Verify user credentials against database
        // For now, return mock token

        String accessToken = generateAccessToken(request.getEmail());
        String refreshToken = generateRefreshToken(request.getEmail());

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .expiresIn(jwtExpiration / 1000) // Convert to seconds
                .tokenType("Bearer")
                .build();
    }

    /**
     * Register new user
     */
    public void register(RegisterRequest request) {
        log.info("Registering new user: {}", request.getEmail());

        // TODO: Save user to database
        // Hash password with passwordEncoder
        // Create user record

        log.info("User registered successfully: {}", request.getEmail());
    }

    /**
     * Refresh access token
     */
    public LoginResponse refreshToken(RefreshTokenRequest request) {
        log.info("Refreshing token");

        // TODO: Validate refresh token and get user info
        String email = "user@example.com"; // Extract from refresh token

        String newAccessToken = generateAccessToken(email);

        return LoginResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(request.getRefreshToken())
                .expiresIn(jwtExpiration / 1000)
                .tokenType("Bearer")
                .build();
    }

    /**
     * Validate JWT token
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8)))
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.error("Token validation failed: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Logout - invalidate token
     */
    public void logout(String token) {
        log.info("User logout");
        // TODO: Add token to blacklist in Redis
    }

    /**
     * Generate JWT access token
     */
    private String generateAccessToken(String email) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", email);
        claims.put("type", "access");

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS512)
                .compact();
    }

    /**
     * Generate JWT refresh token
     */
    private String generateRefreshToken(String email) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", email);
        claims.put("type", "refresh");

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshExpiration))
                .signWith(Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS512)
                .compact();
    }
}
