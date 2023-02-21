package com.example.user_web_service.config;

import com.example.user_web_service.helper.Constant;
import com.example.user_web_service.security.SecurityFilter;

import com.example.user_web_service.security.google.LoginOauth2Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	LoginOauth2Service loginOauth2Service;
	@Autowired
	SecurityFilter securityFilter;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.cors().and().csrf().disable();

		//đăng nhập bằng google
		http.authorizeHttpRequests()
				.antMatchers("/oauth2/**")
				.permitAll().and()
				.oauth2Login();



		http.authorizeHttpRequests().antMatchers("/swagger-ui/**", "/v3/api-docs/**",
				"/error", "/v2/api-docs/**", "/api/v1/notification/**").permitAll();
		// apis that need Admin Role to call
		http.authorizeHttpRequests()
				.antMatchers("/user/list").hasAnyAuthority(Constant.ADMIN_ROLE);

		// api that need User or Admin role to call
		http.authorizeHttpRequests()
				.antMatchers("/user/change-password", "/user/profile", "/user/update",
				"/user/resetPassword")
				.hasAnyAuthority(Constant.USER_ROLE, Constant.ADMIN_ROLE);

		http.addFilterBefore(securityFilter, BasicAuthenticationFilter.class);

		return http.build();
	}
}
