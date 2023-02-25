package com.example.user_web_service.security.jwt;


import com.example.user_web_service.entity.BlackAccessToken;

import java.time.Instant;
import java.util.List;

public interface BlackAccessTokenService {
    BlackAccessToken findByAccessToken(String accessToken);

    List<BlackAccessToken> findAllByExpiryDateBefore(Instant instant);

    BlackAccessToken save(BlackAccessToken blackAccessToken);

    void deleteAllByAccessTokenIn(List<String> accessTokens);
}

