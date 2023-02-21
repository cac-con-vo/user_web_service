package com.example.user_web_service.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.user_web_service.entity.User;


import com.example.user_web_service.helper.Constant;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class SecurityUtils {
	public String GenerateJwt(User user) {
		String jwt = JWT.create().withSubject(user.getUsername()).withIssuer(user.getId().toString())
				.withExpiresAt(new Date(System.currentTimeMillis() + Constant.ACCESS_TIME_EXPIRED))
				.withClaim("role", user.getRole().getName()).sign(Constant.ENCODE_ALGORITHM);
		return jwt;
	}

	public static DecodedJWT decodedJWT(String RawJwt) {
		try {
			JWTVerifier jwtVerifier = JWT.require(Constant.ENCODE_ALGORITHM).build();
			DecodedJWT decodedJWT = jwtVerifier.verify(RawJwt);

			return decodedJWT;
		} catch (Exception e) {
			return null;
		}
	}
}
