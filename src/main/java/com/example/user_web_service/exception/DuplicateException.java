package com.example.user_web_service.exception;

@SuppressWarnings("serial")
public class DuplicateException extends RuntimeException {
	public DuplicateException(String message) {
		super(message);
	}
}
