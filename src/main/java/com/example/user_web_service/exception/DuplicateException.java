package com.example.user_web_service.exception;

import com.example.user_web_service.payload.response.ErrorResponse;
import org.springframework.http.HttpStatus;

import java.util.Date;

@SuppressWarnings("serial")
public class DuplicateException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    public DuplicateException(String message) {
        super(message);
    }
}
