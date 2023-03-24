package com.example.user_web_service.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    @Operation(summary = "Home page of web")
    @GetMapping("/homepages")
    public String index() {

        return "Welcome to my app!";
    }
}
