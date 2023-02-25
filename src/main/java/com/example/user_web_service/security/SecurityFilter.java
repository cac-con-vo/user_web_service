//package com.example.user_web_service.security;
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.Map;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import com.auth0.jwt.interfaces.DecodedJWT;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//
//@Component
//public class SecurityFilter extends OncePerRequestFilter {
//	private static final Logger logger = LoggerFactory.getLogger(SecurityFilter.class);
//	@Autowired
////	private MyAuthentication myAuthentication;
//
//	@Override
//	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//			throws ServletException, IOException {
//
//		try {
////			String token = myAuthentication.getJWT(request);
////			if (token != null && jwtProvider.validateToken(token) && null == blackAccessTokenService.findByAccessToken(DigestUtils.sha3_256Hex(token))){
////				String email = jwtProvider.getEmailFromToken(token);
////				UserDetails userDetails = userDetailService.loadUserByUsername(email);
////				if(userDetails.isEnabled() && userDetails.isAccountNonExpired() && userDetails.isAccountNonLocked()) {
////					UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
////							userDetails, null, userDetails.getAuthorities()
////					);
////					authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
////					SecurityContextHolder.getContext().setAuthentication(authenticationToken);
////				}
////			}
//		}catch (Exception e){
//			logger.error("Can't set user authentication -> Message: ", e);
//		}
//		filterChain.doFilter(request, response);
//
//	}
//
//
//}