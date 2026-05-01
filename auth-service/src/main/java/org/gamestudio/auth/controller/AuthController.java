package org.gamestudio.auth.controller;

import lombok.RequiredArgsConstructor;
import org.gamestudio.auth.dto.AuthRequest;
import org.gamestudio.auth.dto.AuthResponse;
import org.gamestudio.auth.dto.RefreshRequest;
import org.gamestudio.auth.service.JwtService;
import org.gamestudio.auth.service.RefreshTokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.security.InvalidKeyException;

@RestController
@RequestMapping("/api/v1/auth/")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshService;

    @PostMapping("login")
    public AuthResponse login(@RequestBody AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );

        String access = jwtService.generateAccessToken(request.username(),
                (User) authentication.getPrincipal());
        String refresh = refreshService.create(request.username());

        return new AuthResponse(access, refresh);
    }

    @PostMapping("refresh")
    public AuthResponse refresh(@RequestBody RefreshRequest request) throws InvalidKeyException {
        String username = refreshService.verify(request.refreshToken());

        String newAccess = jwtService.generateAccessToken(username);

        return new AuthResponse(newAccess, request.refreshToken());
    }

    @DeleteMapping("logout")
    public ResponseEntity<Void> logout(@RequestBody RefreshRequest request) {
        refreshService.delete(request.refreshToken());

        return ResponseEntity.ok().build();
    }
}
