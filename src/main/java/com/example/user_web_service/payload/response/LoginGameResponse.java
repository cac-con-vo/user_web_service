package com.example.user_web_service.payload.response;

import com.example.user_web_service.form.GameTokenForm;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginGameResponse {
    private String status;
    private String message;
    private String gameToken;
    private String nameOfUser;
}
