package com.example.user_web_service.dto;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Builder
@Getter
public class GameServerInfoDTO {
    private String serverName;
    private String characterName;
    private int currentLevel;
    private List<UserInGameDTO> users;
}
