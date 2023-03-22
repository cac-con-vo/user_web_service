package com.example.user_web_service.config;

import com.example.user_web_service.helper.Constant;
import com.example.user_web_service.security.CustomUserDetailService;

import com.example.user_web_service.security.jwt.JwtEntryPoint;
import com.example.user_web_service.security.jwt.JwtTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


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



		//Accept not need authenticate
		http.authorizeRequests().antMatchers("/swagger-ui/**", "/v3/api-docs/**","/api/v1/auth/login", "/api/v1/auth/accessToken",
				"/error", "/v2/api-docs/**", "/api/v1/notification/**", "/users/signUp","/users/change-password","/api/v1/auth/loginGame", "/users/profile", "/users/update",
						"/users/resetPassword","/api/v1/gameServer/createGameServer", "/api/v1/character/createCharacter", "/api/v1/character/getCharacter",
						"/api/v1/character/getAttributeEffect",
						"/api/v1/character/getAllLevelOfGame",
						"/api/v1/game/saveGame", "/api/v1/game/loadGame")
				.permitAll();

		// apis that need Admin Role to call
		http.authorizeRequests()
				.antMatchers( "/users/list","/api/v1/gameServer/getAllGameServer", "/api/v1/gameServer/updateStatus").hasAnyAuthority(Constant.ADMIN_ROLE);

		// api that need User or Admin role to call
		http.authorizeRequests()
				.antMatchers( "/files/**")
				.hasAnyAuthority(Constant.USER_ROLE, Constant.ADMIN_ROLE)
				;

		//api that need User role to call
		http.authorizeRequests().antMatchers(


		).hasAnyAuthority(Constant.USER_ROLE).anyRequest().authenticated();

		//http.authorizeRequests().anyRequest().authenticated();
		http.addFilterBefore(jwtTokenFilter(), BasicAuthenticationFilter.class);

		return http.build();
	}

}
