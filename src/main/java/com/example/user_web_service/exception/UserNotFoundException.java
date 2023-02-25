package com.example.user_web_service.exception;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserNotFoundException extends UsernameNotFoundException {
     public UserNotFoundException(String message) {
            super(message);
        }
}
