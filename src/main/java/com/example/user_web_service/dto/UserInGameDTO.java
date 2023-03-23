package com.example.user_web_service.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Builder
@Getter
public class UserInGameDTO {
    private Long id;
    private String username;
}
