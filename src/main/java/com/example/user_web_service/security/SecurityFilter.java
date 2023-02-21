package com.example.user_web_service.security;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;


@Component
public class SecurityFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		if (request.getServletPath().equals("/Welcome/Login")
				|| request.getServletPath().equals("/Welcome/RefreshToken")) {
			filterChain.doFilter(request, response);
		} else {
			String authorizationHeader = request.getHeader("Authorization");
			if (authorizationHeader != null) {
				System.out.println("authorizationHeader: " + authorizationHeader);
				String token = authorizationHeader;

				DecodedJWT decodedToken = SecurityUtils.decodedJWT(token);
				if (decodedToken != null) {
					MyAuthentication myAuthentication = new MyAuthentication(token);
					myAuthentication.setAuthenticated(true);

					SecurityContextHolder.getContext().setAuthentication(myAuthentication);
					filterChain.doFilter(request, response);
				} else {
					HttpStatus status = HttpStatus.REQUEST_TIMEOUT;

					response.setStatus(status.value());

					Map<String, String> errors = new HashMap<>();
					errors.put("response", "This Access Token Expired!");
					errors.put("status", String.valueOf(status.value()));

					response.setContentType(MediaType.APPLICATION_JSON_VALUE);
					new ObjectMapper().writeValue(response.getOutputStream(), errors);
				}
			} else {
				System.out.println("no authorizationHeader found");
				filterChain.doFilter(request, response);
			}
		}

	}


}