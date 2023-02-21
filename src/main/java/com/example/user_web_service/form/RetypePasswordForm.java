package com.example.user_web_service.form;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RetypePasswordForm {
    private String code;
    private String email;
    private String password;

    @Override
    public String toString() {
        return "RetypePasswordForm [code=" + code + ", email=" + email + ", password=" + password + "]";
    }
}
