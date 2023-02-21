package com.example.user_web_service.controller;


import com.example.user_web_service.helper.Constant;
import com.example.user_web_service.security.MyAuthentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;

public class BaseController {
	public Long getCurrentUserId() {
		try {
			SecurityContext securityContext = SecurityContextHolder.getContext();
			MyAuthentication authentication = (MyAuthentication) securityContext.getAuthentication();
			return (Long) authentication.getCredentials();
		} catch (Exception e) {
			return -1L;
		}
	}

	public boolean isAdmin() {
		try {
			SecurityContext securityContext = SecurityContextHolder.getContext();
			MyAuthentication authentication = (MyAuthentication) securityContext.getAuthentication();
			return Objects.equals(authentication.getDetails(), Constant.ADMIN_ROLE);
		} catch (Exception e) {
			return false;
		}
	}

	public boolean isUser() {
		try {
			SecurityContext securityContext = SecurityContextHolder.getContext();
			MyAuthentication authentication = (MyAuthentication) securityContext.getAuthentication();
			return Objects.equals(authentication.getDetails(), Constant.USER_ROLE);
		} catch (Exception e) {
			return false;
		}
	}
}
