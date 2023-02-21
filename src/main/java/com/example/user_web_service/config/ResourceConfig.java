package com.example.user_web_service.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

@Configuration
@CrossOrigin
@EnableWebMvc
@EnableAsync
public class ResourceConfig implements WebMvcConfigurer {
	@Autowired
	Environment environment;

	public static String serverPort = "";

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		serverPort = environment.getProperty("server.port");
		String location = "file:///" + Paths.get("./public/").normalize().toAbsolutePath().toString().replace("\\", "/")
				+ "/";
		registry.addResourceHandler("/public/**").addResourceLocations(location);
	}

}
