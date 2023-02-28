package com.example.user_web_service.controller;

import com.example.user_web_service.dto.ResponseObject;
import com.example.user_web_service.form.GameTokenForm;
import com.example.user_web_service.form.LoginForm;
import com.example.user_web_service.form.LogoutForm;
import com.example.user_web_service.form.RefreshTokenForm;
import com.example.user_web_service.service.impl.AuthServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin
public class AuthController {
    @Autowired
    private AuthServiceImpl authService;

    @Operation(summary = "For login", description = "test")
    @PostMapping("/auth/login")
    @SecurityRequirements
    public ResponseEntity<ResponseObject> login(@Valid @RequestBody LoginForm loginForm) {
        if(loginForm.getUsername().isEmpty() || loginForm.getUsername().isBlank() || loginForm.getUsername() == null){
            return new ResponseEntity<ResponseObject>(new ResponseObject(HttpStatus.BAD_REQUEST.toString(), "Please input username", null, null), HttpStatus.BAD_REQUEST);
        }
        if(loginForm.getPassword().isEmpty() || loginForm.getPassword().isBlank() || loginForm.getPassword() == null){
            return new ResponseEntity<ResponseObject>(new ResponseObject(HttpStatus.BAD_REQUEST.toString(), "Please input password", null, null), HttpStatus.BAD_REQUEST);
        }
        return authService.login(loginForm);
    }
    @PostMapping("/auth/validation")
    @Operation(summary = "For getting user information after login")
    public ResponseEntity<ResponseObject> reloadUserByJWT() {
        return authService.validateAccessToken();
    }

    @PostMapping("/auth/accessToken")
    @Operation(summary = "For getting new access token by refresh token after it expired")
    public ResponseEntity<ResponseObject> refreshAccessToken(HttpServletRequest request, @Valid @RequestBody RefreshTokenForm refreshTokenForm) {
        if(refreshTokenForm.getRefreshToken() == null || refreshTokenForm.getRefreshToken().isEmpty() || refreshTokenForm.getRefreshToken().isBlank()){
            return new ResponseEntity<ResponseObject>(new ResponseObject(HttpStatus.BAD_REQUEST.toString(), "Please input refresh token", null, null), HttpStatus.BAD_REQUEST);
        }
        return authService.refreshAccessToken(request, refreshTokenForm);
    }

    @PostMapping("/auth/logout")
    @Operation(summary = "For logout")
    public ResponseEntity<ResponseObject> logout(HttpServletRequest request, @Valid @RequestBody LogoutForm logoutForm) {
        if(logoutForm.getRefreshToken() == null || logoutForm.getRefreshToken().isEmpty() || logoutForm.getRefreshToken().isBlank()){
            return new ResponseEntity<ResponseObject>(new ResponseObject(HttpStatus.BAD_REQUEST.toString(), "Please input refresh token", null, null), HttpStatus.BAD_REQUEST);
        }
        return authService.logout(request, logoutForm);
    }

    @PostMapping("/auth/loginGame")
    @Operation(summary = "For login in game")
    public ResponseEntity<ResponseObject> loginGame(@Valid @RequestBody GameTokenForm gameTokenForm) {
        if(gameTokenForm.getGameToken() == null || gameTokenForm.getGameToken().isEmpty() || gameTokenForm.getGameToken().isBlank()){
            return new ResponseEntity<ResponseObject>(new ResponseObject(HttpStatus.BAD_REQUEST.toString(), "Please input refresh token", null, null), HttpStatus.BAD_REQUEST);
        }
        return authService.loginGame(gameTokenForm);
    }
}
