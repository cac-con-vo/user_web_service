package com.example.user_web_service.config;

import com.example.user_web_service.helper.Constant;
import com.example.user_web_service.security.CustomUserDetailService;

import com.example.user_web_service.security.google.LoginOauth2Service;
import com.example.user_web_service.security.jwt.JwtEntryPoint;
import com.example.user_web_service.security.jwt.JwtTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	LoginOauth2Service loginOauth2Service;
	@Autowired
	CustomUserDetailService userDetailsService;

	@Autowired
	private JwtEntryPoint jwtEntryPoint;

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	@Bean
	public JwtTokenFilter jwtTokenFilter() {
		return new JwtTokenFilter();
	}
	@Bean
	public DefaultAuthenticationEventPublisher authenticationEventPublisher() {
		return new DefaultAuthenticationEventPublisher();
	}
	@Bean
	public AuthenticationManager authManager(HttpSecurity httpSecurity) throws Exception {
		return httpSecurity.getSharedObject(AuthenticationManagerBuilder.class)
				.authenticationEventPublisher(authenticationEventPublisher())
				.userDetailsService(userDetailsService)
				.passwordEncoder(passwordEncoder())
				.and()
				.build();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.cors().and().csrf().disable();

		//đăng nhập bằng google
		http.authorizeRequests()
				.antMatchers("/oauth2/**")
				.permitAll().and()
				.oauth2Login();


		//Accept not need authenticate
		http.authorizeRequests().antMatchers("/swagger-ui/**", "/v3/api-docs/**","/api/v1/auth/login", "/api/v1/auth/accessToken",
				"/error", "/v2/api-docs/**", "/api/v1/notification/**", "/users/signUpa")
				.permitAll();

		// apis that need Admin Role to call
		http.authorizeRequests()
				.antMatchers("/users/list", "/api/v1/gameServer/getAllGameServer").hasAnyAuthority(Constant.ADMIN_ROLE);

		// api that need User or Admin role to call
		http.authorizeRequests()
				.antMatchers("/users/change-password", "/users/profile", "/users/update",
				"/users/resetPassword", "/api/v1/gameServer/getAllGameServerOfUser")
				.hasAnyAuthority(Constant.USER_ROLE, Constant.ADMIN_ROLE);

		//api that need User role to call
		http.authorizeRequests().antMatchers(
			"/api/v1/gameServer/createGameServer", "/api/v1/character/createCharacter","/api/v1/auth/loginGame",
				"/api/v1/gameServer/addUserToGame", "/api/v1/character/getCharacter"
		).hasAnyAuthority(Constant.USER_ROLE);

		http.authorizeRequests().anyRequest().authenticated();
		http.addFilterBefore(jwtTokenFilter(), BasicAuthenticationFilter.class);

		return http.build();
	}

}
