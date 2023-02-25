package com.example.user_web_service.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginForm {
    @Schema(description = "input username",example = "admin")
    private String username;

    @Schema(description = "input user password",example = "admin123456")
    @Size(min = 6, max = 60)
    private String password;
}

