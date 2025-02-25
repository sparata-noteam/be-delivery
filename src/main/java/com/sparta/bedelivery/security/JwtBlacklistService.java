package com.sparta.bedelivery.security;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class JwtBlacklistService {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final String BLACKLIST_PREFIX = "blacklist:";

    /**
     * 토큰을 블랙리스트에 추가 (만료 시간만큼 유지)
     * @param token 블랙리스트에 추가할 JWT 토큰
     * @param expirationMillis 만료까지 남은 시간 (밀리초 단위)
     */
    public void addToBlacklist(String token, long expirationMillis) {
        redisTemplate.opsForValue().set(BLACKLIST_PREFIX + token, "logout", expirationMillis, TimeUnit.MILLISECONDS);
    }

    /**
     * 토큰이 블랙리스트에 있는지 확인
     * @param token 확인할 JWT 토큰
     * @return 블랙리스트에 있으면 true, 없으면 false
     */
    public boolean isBlacklisted(String token) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(BLACKLIST_PREFIX + token));
    }

    public void storeRefreshToken(String userId, String refreshToken, int days) {
        redisTemplate.opsForValue().set("refresh:" + userId, refreshToken, days, TimeUnit.DAYS);
    }

    public String getRefreshToken(String userId) {
        return (String) redisTemplate.opsForValue().get("refresh:" + userId);
    }

    public void deleteRefreshToken(String userId) {
        redisTemplate.delete("refresh:" + userId);
    }

}
