package com.example.user_web_service.form;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter

public class UpdateUserForm {

    private String password;

    private String firstname;

    private String lastname;

    private String email;

    private String phone;
    private String dob;
}
