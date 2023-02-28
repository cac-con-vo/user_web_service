package com.example.user_web_service.security.jwt;

import com.example.user_web_service.entity.GameToken;
import com.example.user_web_service.exception.ResourceNotFoundException;
import com.example.user_web_service.repository.GameTokenRepository;
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
public class GameTokenProvider {
    @Autowired
    UserRepository userRepository;
    @Autowired
    GameTokenRepository gameTokenRepository;

    @Autowired
    CacheManager cacheManager;
    @Value("${app.gameTokenDurationMs}")
    long gameTokenDurationMs;

    @Cacheable("gameToken")
    public Optional<GameToken> findByToken(String token) {
        return gameTokenRepository.findByToken(token);
    }


    public GameToken createGameToken(String username) {
        GameToken gameToken = new GameToken();
        gameToken.setUser(userRepository.findByUsername(username).get());

        gameToken.setExpiryDate(Instant.now().plusMillis(gameTokenDurationMs));
        gameToken.setToken(UUID.randomUUID().toString());

        GameToken gameTokenEncrypt = new GameToken(gameToken);
        gameTokenEncrypt.setToken(DigestUtils.sha3_256Hex(gameToken.getToken()));

        cacheManager.getCache("gameToken").put(DigestUtils.sha3_256Hex(gameToken.getToken()), gameToken );

        gameTokenRepository.save(gameTokenEncrypt);
        return gameToken;
    }


    public GameToken verifyExpiration(GameToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            gameTokenRepository.delete(token);
            throw new GameTokenException("Game token was expired");
        }
        return token;
    }

    @Transactional
    public GameToken deleteByUserId(Long userId) {
        GameToken gameToken = gameTokenRepository.findByUser_Id(userId).orElseThrow(() -> new RuntimeException("hi"));
        gameTokenRepository.deleteAllByUser_Id(userId);
        return gameToken;
    }
//    public boolean authenticateGameToken(String token) {
//        GameToken gameToken = cacheManager.getCache("gameToken").get(DigestUtils.sha3_256Hex(token), GameToken.class);
//        if (gameToken != null && gameToken.getExpiryDate().isAfter(Instant.now())) {
//            String encryptedToken = DigestUtils.sha3_256Hex(token);
//            GameToken gameTokenEncrypt = gameTokenRepository.findByToken(encryptedToken).orElseThrow(()->new ResourceNotFoundException("Game token ", null, token));
//            if (gameTokenEncrypt != null && gameTokenEncrypt.getExpiryDate().isAfter(Instant.now())) {
//                return true;
//            }
//        }
//        return false;
//    }

    @Transactional
    public void deleteByToken(String token) {
        gameTokenRepository.deleteByToken(token);
    }
}
