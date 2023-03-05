package com.example.user_web_service.dto;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LevelDTO {
    private int levelValue;
    private Long levelRequire;
}
