package com.example.user_web_service.dto;

import lombok.*;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Builder
@Getter
public class UserDTO {
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String avatar;
}
