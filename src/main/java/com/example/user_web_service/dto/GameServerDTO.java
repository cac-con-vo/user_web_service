package com.example.user_web_service.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Builder
@Getter
public class GameServerDTO {
    private Long id;
    private String name;
}
