package com.example.user_web_service.security.jwt;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class GameTokenException extends RuntimeException{
    private static final long serialVersionUID = 1L;
    public GameTokenException(String message) {
        super(String.format(message));
    }
}