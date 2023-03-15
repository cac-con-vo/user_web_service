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

public class UpdateUserForm {
    @Schema(description = "First Name of user", example = "Van",required = true)
    @Size(max = 20)
    @NotBlank(message = "firstName not null")
    private String firstname;
    @Schema(description = "Last Name of user", example = "Nguyen",required = true)
    @Size(max = 20)
    @NotBlank(message = "lastName not null")
    private String lastname;
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
