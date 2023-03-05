package com.example.user_web_service.dto;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LevelProgressDTO {
    private int currentLevel;
    private long levelPoint;
}
