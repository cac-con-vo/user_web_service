package com.example.user_web_service.exception;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserNotFoundException extends UsernameNotFoundException {
    private static final long serialVersionUID = 1L;
    public UserNotFoundException(String username, String message) {
        super(String.format("Failed for [%s]: %s", username, message));
    }
}
