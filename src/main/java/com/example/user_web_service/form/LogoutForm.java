package com.example.user_web_service.form;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.codec.digest.DigestUtils;

import javax.validation.constraints.Pattern;

@NoArgsConstructor
@AllArgsConstructor
@Setter
public class LogoutForm {
    @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$")
    private String refreshToken;

    public String getRefreshToken() {
        return DigestUtils.sha3_256Hex(refreshToken);
    }
}

