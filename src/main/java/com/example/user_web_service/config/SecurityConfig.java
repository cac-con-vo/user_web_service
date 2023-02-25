package com.example.user_web_service.config;

import com.example.user_web_service.helper.Constant;
import com.example.user_web_service.security.CustomUserDetailService;

import com.example.user_web_service.security.google.LoginOauth2Service;
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
		http.authorizeHttpRequests()
				.antMatchers("/oauth2/**")
				.permitAll().and()
				.oauth2Login();



		http.authorizeHttpRequests().antMatchers("/swagger-ui/**", "/v3/api-docs/**","/api/v1/auth/**",
				"/error", "/v2/api-docs/**", "/api/v1/notification/**").permitAll();
		// apis that need Admin Role to call
		http.authorizeHttpRequests()
				.antMatchers("/users/list").hasAnyAuthority(Constant.ADMIN_ROLE);

		// api that need User or Admin role to call
		http.authorizeHttpRequests()
				.antMatchers("/users/change-password", "/users/profile", "/users/update",
				"/users/resetPassword")
				.hasAnyAuthority(Constant.USER_ROLE, Constant.ADMIN_ROLE);

		http.addFilterBefore(jwtTokenFilter(), BasicAuthenticationFilter.class);

		return http.build();
	}
}
