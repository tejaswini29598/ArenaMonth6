package com.enterprise.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Login Request DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    private String email;
    private String password;
}

/**
 * Login Response DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class LoginResponse {
    private String accessToken;
    private String refreshToken;
    private Long expiresIn;
    private String tokenType = "Bearer";
}

/**
 * Token Refresh Request DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class RefreshTokenRequest {
    private String refreshToken;
}

/**
 * Registration Request DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class RegisterRequest {
    private String email;
    private String password;
    private String name;
    private String role = "USER";
}
