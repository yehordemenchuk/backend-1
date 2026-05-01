package org.gamestudio.auth.dto;

public record AuthResponse(String accessToken,
                           String refreshToken) {
}
