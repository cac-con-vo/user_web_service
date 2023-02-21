package com.example.user_web_service.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;

@SuppressWarnings("serial")
public class MyAuthentication implements Authentication {

	private String JWT;

	private Boolean isAuth = false;

	public MyAuthentication(String jWT) {
		super();
		JWT = jWT;
	}

	public String getJWT() {
		return JWT;
	}

	public void setJWT(String jWT) {
		JWT = jWT;
	}

	@Override
	public String getName() {
		try {
			return SecurityUtils.decodedJWT(JWT).getSubject();
		} catch (Exception e) {
			return "";
		}
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		try {
			String role = SecurityUtils.decodedJWT(JWT).getClaim("role").asString();

			Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
			authorities.add(new SimpleGrantedAuthority(role));

			return authorities;
		} catch (Exception e) {
			return new ArrayList<>();
		}
	}

	@Override
	public Object getCredentials() {
		try {
			String issuer = SecurityUtils.decodedJWT(JWT).getIssuer();
			return Long.parseLong(issuer);
		} catch (Exception e) {
			return "";
		}
	}

	@Override
	public Object getDetails() {
		return SecurityUtils.decodedJWT(JWT).getClaim("role").asString();
	}

	@Override
	public Object getPrincipal() {
		return null;
	}

	@Override
	public boolean isAuthenticated() {
		return isAuth;
	}

	@Override
	public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
		isAuth = isAuthenticated;
	}

}
