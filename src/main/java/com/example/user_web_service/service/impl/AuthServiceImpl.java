package com.example.user_web_service.service.impl;

import com.example.user_web_service.dto.PrincipalDTO;
import com.example.user_web_service.dto.ResponseObject;
import com.example.user_web_service.entity.BlackAccessToken;
import com.example.user_web_service.entity.GameServer;
import com.example.user_web_service.entity.GameToken;
import com.example.user_web_service.entity.RefreshToken;
import com.example.user_web_service.form.GameTokenForm;
import com.example.user_web_service.form.LoginForm;
import com.example.user_web_service.form.LogoutForm;
import com.example.user_web_service.form.RefreshTokenForm;
import com.example.user_web_service.payload.response.RefreshTokenResponse;
import com.example.user_web_service.repository.GameServerRepository;
import com.example.user_web_service.repository.UserRepository;
import com.example.user_web_service.security.jwt.*;
import com.example.user_web_service.security.userprincipal.Principal;
import com.example.user_web_service.service.AuthService;
import com.example.user_web_service.service.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import org.apache.commons.codec.digest.DigestUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.List;

@Service
public class AuthServiceImpl implements AuthService {
    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    private final String companyEmail = "FA.HCM@fsoft.com.vn";
    @Autowired
    private final ModelMapper mapper;
    @Autowired
    CacheManager cacheManager;
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RefreshTokenProvider refreshTokenProvider;
    @Autowired
    private GameTokenProvider gameTokenProvider;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private BlackAccessTokenServiceImp blackAccessTokenServiceImp;
    @Autowired
    private UserService userService;
    @Autowired
    private GameServerRepository gameServerRepository ;

    public AuthServiceImpl(ModelMapper mapper) {
        this.mapper = mapper;
    }
    @Override
    public ResponseEntity<ResponseObject> validateLoginForm(LoginForm loginForm) {
        if ((loginForm.getUsername().isEmpty() || loginForm.getUsername().isBlank()) && (loginForm.getPassword().isEmpty() || loginForm.getPassword().isBlank())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject(HttpStatus.BAD_REQUEST.toString(), "Empty input field!", null, null));
        } else if (loginForm.getUsername().isEmpty() || loginForm.getUsername().isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject(HttpStatus.BAD_REQUEST.toString(), "Empty username!", null, null));
        } else if (loginForm.getPassword().isEmpty() || loginForm.getPassword().isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject(HttpStatus.BAD_REQUEST.toString(), "Empty password!", null, null));
        }
        return null;
    }

    @Override
    public ResponseEntity<ResponseObject> login(LoginForm loginForm) {
        ResponseEntity<ResponseObject> responseEntity = this.validateLoginForm(loginForm);
        if (responseEntity != null) {
            return responseEntity;
        }

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginForm.getUsername(), loginForm.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            Principal userPrinciple = (Principal) authentication.getPrincipal();
            String accessToken = jwtProvider.createToken(userPrinciple);
            String refreshToken = refreshTokenProvider.createRefreshToken(loginForm.getUsername()).getToken();
            String gameToken = gameTokenProvider.createGameToken(loginForm.getUsername()).getToken();
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObject(HttpStatus.ACCEPTED.toString(), "Login success!", null, new JwtResponse(accessToken, refreshToken, gameToken)));
        } catch (AuthenticationException e) {
            if (e instanceof DisabledException) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObject(HttpStatus.UNAUTHORIZED.toString(), "Account has been locked. Please contact " + companyEmail + " for more information", null, null));
            }
            if(e instanceof AccountExpiredException){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObject(HttpStatus.UNAUTHORIZED.toString(), "The account has expired. Please contact " + companyEmail + " for more information", null, null));
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObject(HttpStatus.UNAUTHORIZED.toString(), "Invalid email or password. Please try again.", null, null));
        }
    }

    @Override
    public ResponseEntity<ResponseObject> validateAccessToken() {
        Principal userPrinciple = (Principal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        PrincipalDTO principalDTO = mapper.map(userPrinciple, PrincipalDTO.class);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObject(HttpStatus.ACCEPTED.toString(), "Validate access token success!", null, principalDTO));
    }

    @Override
    public ResponseEntity<ResponseObject> refreshAccessToken(HttpServletRequest request, RefreshTokenForm refreshTokenForm) {
        String accessToken = jwtProvider.getJwt(request);
        try {
            jwtProvider.validateTokenThrowException(accessToken);
        } catch (Exception e) {
            if (e instanceof ExpiredJwtException) {
                return refreshTokenProvider.findByToken(DigestUtils.sha3_256Hex(refreshTokenForm.getRefreshToken()))
                        .map(refreshTokenProvider::verifyExpiration)
                        .map(RefreshToken::getUser)
                        .map(user -> {
                            Principal principal = Principal.build(user);
                            String newAccessToken = jwtProvider.createToken(principal);
                            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObject(HttpStatus.ACCEPTED.toString(), "Refresh token success!", null, new RefreshTokenResponse(newAccessToken, refreshTokenForm.getRefreshToken())));
                        })
                        .orElseThrow(() -> new RefreshTokenException("Refresh token is not in database!"));
            }
            throw new JwtTokenException("Error -> Unauthorized");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObject(HttpStatus.UNAUTHORIZED.toString(), "Cannot create new access token -> Old access token is not expired", null, null));
    }

    @Override
    public ResponseEntity<ResponseObject> logout(HttpServletRequest request, LogoutForm logoutForm) {
        //delete refresh token
        refreshTokenProvider.deleteByToken(logoutForm.getRefreshToken());
        //set access token into black list to prevent reused.
        String accessToken = jwtProvider.getJwt(request);
        Instant expiredTime = jwtProvider.getAccessTokenExpiredTime(accessToken).toInstant();
        BlackAccessToken blackAccessToken = BlackAccessToken.builder()
                .accessToken(DigestUtils.sha3_256Hex(accessToken))
                .expiryDate(expiredTime)
                .build();
        blackAccessTokenServiceImp.save(blackAccessToken);

        //For cache
        this.clearRefreshTokenCache(logoutForm.getRefreshToken());
        this.clearUserDetailsCache(jwtProvider.getUsernameFromToken(accessToken));

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObject(HttpStatus.ACCEPTED.toString(), "Logout success!", null, new RefreshTokenResponse(null, null)));
    }

    @Override
    public ResponseEntity<ResponseObject> loginGame(GameTokenForm gameTokenForm) {
            return  gameTokenProvider.findByToken(DigestUtils.sha3_256Hex(gameTokenForm.getGameToken()))
                    .map(gameTokenProvider::verifyExpiration)
                    .map(GameToken::getUser)
                    .map(user -> {
                        List<GameServer> gameServers =  gameServerRepository.findAllByUsers(user);
                        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObject(HttpStatus.ACCEPTED.toString(), "Get list game server success!", null, gameServers));
                    })
                    .orElseThrow(() -> new RefreshTokenException("Game token is not in database!"));

    }


    private void clearRefreshTokenCache(String refreshToken) {
        boolean result = cacheManager.getCache("refreshToken").evictIfPresent(refreshToken);
        if (result) {
            logger.info("Clear refresh token " + refreshToken + " from cache");
        } else {
            logger.error("Fail clear refresh token " + refreshToken + " from cache");
        }
    }

    private void clearUserDetailsCache(String username) {
        boolean result = cacheManager.getCache("userDetails").evictIfPresent(username);
        if (result) {
            logger.info("Clear account " + username + " from cache");
        } else {
            logger.error("Fail clear account " + username + " from cache");
        }
    }
}
