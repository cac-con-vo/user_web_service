package com.example.user_web_service.service;

import com.example.user_web_service.dto.ResponseObject;
import com.example.user_web_service.form.*;
import com.example.user_web_service.payload.response.LoginGameResponse;
import com.example.user_web_service.security.userprincipal.Principal;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.net.UnknownHostException;

public interface AuthService {
    ResponseEntity<ResponseObject> validateLoginForm(LoginForm loginForm);
    ResponseEntity<ResponseObject> login(LoginForm LoginForm);

    ResponseEntity<ResponseObject> validateAccessToken();
    ResponseEntity<LoginGameResponse> validateAccessTokenForLoginGame(HttpServletRequest request, AccessTokenForm accessTokenForm) throws UnknownHostException;
    ResponseEntity<ResponseObject> refreshAccessToken(HttpServletRequest request, RefreshTokenForm refreshTokenForm);
    ResponseEntity<ResponseObject> logout(HttpServletRequest request, LogoutForm logoutForm);
}
