package com.example.user_web_service.service;

import com.example.user_web_service.dto.ResponseObject;
import com.example.user_web_service.form.GameTokenForm;
import com.example.user_web_service.form.LoginForm;
import com.example.user_web_service.form.LogoutForm;
import com.example.user_web_service.form.RefreshTokenForm;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

public interface AuthService {
    ResponseEntity<ResponseObject> validateLoginForm(LoginForm loginForm);
    ResponseEntity<ResponseObject> login(LoginForm LoginForm);

    ResponseEntity<ResponseObject> validateAccessToken();
    ResponseEntity<ResponseObject> refreshAccessToken(HttpServletRequest request, RefreshTokenForm refreshTokenForm);
    ResponseEntity<ResponseObject> logout(HttpServletRequest request, LogoutForm logoutForm);
    ResponseEntity<ResponseObject> loginGame(GameTokenForm gameTokenForm);
}
