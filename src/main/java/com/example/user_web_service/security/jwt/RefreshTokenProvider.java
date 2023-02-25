package com.example.user_web_service.security.jwt;

import com.example.user_web_service.entity.RefreshToken;
import com.example.user_web_service.repository.RefreshTokenRepository;
import com.example.user_web_service.repository.UserRepository;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Component
public class RefreshTokenProvider {
    @Autowired
    UserRepository userRepository;
    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @Autowired
    CacheManager cacheManager;
    @Value("${app.refreshTokenDurationMs}")
    long refreshTokenDurationMs;

    @Cacheable("refreshToken")
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }


    public RefreshToken createRefreshToken(String username) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(userRepository.findByUsername(username).get());

        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setToken(UUID.randomUUID().toString());

        RefreshToken refreshTokenEncrypt = new RefreshToken(refreshToken);
        refreshTokenEncrypt.setToken(DigestUtils.sha3_256Hex(refreshToken.getToken()));

        cacheManager.getCache("refreshToken").put(DigestUtils.sha3_256Hex(refreshToken.getToken()), refreshToken );

        refreshTokenRepository.save(refreshTokenEncrypt);
        return refreshToken;
    }


    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new RefreshTokenException("Refresh token was expired");
        }
        return token;
    }

    @Transactional
    public RefreshToken deleteByUserId(Long userId) {
        RefreshToken refreshToken = refreshTokenRepository.findByUser_Id(userId).orElseThrow(() -> new RuntimeException("hi"));
        refreshTokenRepository.deleteAllByUser_Id(userId);
        return refreshToken;
    }

    @Transactional
    public void deleteByToken(String token) {
        refreshTokenRepository.deleteByToken(token);
    }
}
