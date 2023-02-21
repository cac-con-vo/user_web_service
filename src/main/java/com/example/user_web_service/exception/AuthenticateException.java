package com.example.user_web_service.exception;

@SuppressWarnings("serial")
public class AuthenticateException extends RuntimeException {
	public AuthenticateException(String message) {
		super(message);
	}
}
