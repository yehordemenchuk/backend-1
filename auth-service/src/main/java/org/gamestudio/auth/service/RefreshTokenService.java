package org.gamestudio.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.stereotype.Service;

import java.security.InvalidKeyException;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private static final String TOKEN_PREFIX = "refresh:";
    private static final String USER_PREFIX = "user_refresh:";

    private final StringRedisTemplate stringRedisTemplate;
    private final JwtService jwtService;

    public String create(String username) {
        deleteAllUsers(username);

        String refreshToken = jwtService.generateRefreshToken(username);

        stringRedisTemplate.opsForValue().set(
                buildTokenKey(refreshToken),
                username,
                jwtService.getRefreshTtlSeconds(),
                TimeUnit.SECONDS
        );

        stringRedisTemplate.opsForSet().add(buildUserKey(username), refreshToken);

        return refreshToken;
    }

    public String verify(String token) throws AuthenticationServiceException, InvalidKeyException {
        if (!jwtService.isValidToken(token)) {
            throw new AuthenticationServiceException("Invalid refresh token. Invalid token.");
        }

        String username = stringRedisTemplate.opsForValue().get(buildTokenKey(token));

        if (Objects.isNull(username)) {
            throw new InvalidKeyException("Invalid refresh token. Invalid username.");
        }

        return username;
    }

    public void delete(String token) {
        String username = stringRedisTemplate.opsForValue().get(buildTokenKey(token));

        if (Objects.nonNull(username)) {
            stringRedisTemplate.opsForSet().remove(buildUserKey(username), token);

            stringRedisTemplate.delete(buildTokenKey(token));
        }
    }

    public void deleteAllUsers(String login) {
        Set<String> tokens = stringRedisTemplate.opsForSet().members(buildUserKey(login));

        if (Objects.nonNull(tokens) && !tokens.isEmpty()) {
            tokens.forEach(token -> stringRedisTemplate.delete(buildTokenKey(token)));

            stringRedisTemplate.delete(buildUserKey(login));
        }
    }

    private String buildTokenKey(String refreshToken) {
        return TOKEN_PREFIX + refreshToken;
    }

    private String buildUserKey(String username) {
        return USER_PREFIX + username;
    }
}
