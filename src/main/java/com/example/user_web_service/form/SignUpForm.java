package com.example.user_web_service.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignUpForm {
    @Schema(description = "username", required = true)
    @Size(min = 5,max = 60)
    @NotBlank(message = "username not null")
    private String username;

    @Schema(description = "password", required = true)
    @Size(min = 6,max = 60)
    @NotBlank(message = "password not null")
    private String password;

    @Schema(description = "First Name of user", example = "Van",required = true)
    @Size(max = 20)
    @NotBlank(message = "firstName not null")
    private String firstName;

    @Schema(description = "Last Name of user", example = "Nguyen",required = true)
    @Size(max = 20)
    @NotBlank(message = "lastName not null")
    private String lastName;
    @Email
    @Schema(description = "Email of user", example = "vanng@gmail.com",required = true)
    @NotBlank(message = "Email not null")
    private String email;
    @Schema(description = "Phone of user", example = "1234567890", required = true)
    @NotBlank(message = "Phone not null")
    @Pattern(regexp = "\\d+", message = "Phone must contain only digits")
    @Size(min = 10, max = 10, message = "Phone must be 10 digits long")
    private String phone;

}
