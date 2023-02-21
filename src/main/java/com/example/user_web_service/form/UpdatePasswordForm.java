package com.example.user_web_service.form;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePasswordForm {
    private String oldPassword;
    private String newPassword;
}
