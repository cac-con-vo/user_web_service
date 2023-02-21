package com.example.user_web_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.HttpMethod;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


import java.util.Arrays;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
				.allowedMethods(HttpMethod.GET.name(), HttpMethod.POST.name(), HttpMethod.PUT.name(),
						HttpMethod.OPTIONS.name(), HttpMethod.DELETE.name())
				.allowedHeaders("*")
				.allowedOrigins("http://localhost:8080");
	}

//	@Bean
//	CorsConfigurationSource corsConfigurationSource() {
//		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//		CorsConfiguration config = new CorsConfiguration();
//
//		config.setAllowCredentials(true);
//		config.setAllowedOriginPatterns(Arrays.asList("http://localhost:8080", "http://localhost:8080/"));
//		config.setAllowedHeaders(Arrays.asList("Accept", "Origin", "Content-Type", "Depth", "User-Agent",
//				"If-Modified-Since,", "Cache-Control", "Authorization", "X-Req", "X-File-Size", "X-Requested-With",
//				"X-File-Name", "Content-Disposition"));
//		// Arrays.asList("Origin", "Content-Type", "Accept")
//		config.setAllowedMethods(Arrays.asList("HEAD", "GET", "POST", "PUT", "OPTIONS", "DELETE", "PATCH"));
//		config.setExposedHeaders(Arrays.asList("Origin", "X-Requested-With", "Content-Type", "Accept", "Authorization",
//				"Content-Disposition"));
//		source.registerCorsConfiguration("/**", config);
//		return source;
//	}
}
