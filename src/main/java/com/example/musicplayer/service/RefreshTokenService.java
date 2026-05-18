package com.example.musicplayer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class RefreshTokenService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Value("${app.jwt.refreshExpiration}")
    private long jwtRefreshExpirationMs;

    private static final String USER_TOKENS_KEY_PREFIX = "UserTokens:";

    public String createRefreshToken(String username) {
        String token = UUID.randomUUID().toString();
        String key = USER_TOKENS_KEY_PREFIX + username;

        // Add token to user's set in Redis
        redisTemplate.opsForSet().add(key, token);

        redisTemplate.expire(key, jwtRefreshExpirationMs, TimeUnit.MILLISECONDS);

        return token;
    }

    public boolean validateRefreshToken(String username, String refreshToken) {
        String key = USER_TOKENS_KEY_PREFIX + username;
        Boolean isMember = redisTemplate.opsForSet().isMember(key, refreshToken);
        return Boolean.TRUE.equals(isMember);
    }

    public void deleteSpecificToken(String username, String refreshToken) {
        String key = USER_TOKENS_KEY_PREFIX + username;
        redisTemplate.opsForSet().remove(key, refreshToken);
    }

    public void deleteAllTokensForUser(String username) {
        String key = USER_TOKENS_KEY_PREFIX + username;
        redisTemplate.delete(key);
    }

    public void revokeAllTokensExcept(String username, String currentToken) {
        String key = USER_TOKENS_KEY_PREFIX + username;

        Set<String> allTokens = redisTemplate.opsForSet().members(key);
        if (allTokens == null || allTokens.isEmpty()) {
            return;
        }
        // Remove every token that is NOT the current one
        allTokens.stream()
                .filter(token -> !token.equals(currentToken))
                .forEach(token -> redisTemplate.opsForSet().remove(key, token));
    }
}
