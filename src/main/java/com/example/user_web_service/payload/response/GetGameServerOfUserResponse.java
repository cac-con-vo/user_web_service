package com.example.user_web_service.payload.response;

import com.example.user_web_service.dto.CharacterDTO;
import com.example.user_web_service.dto.GameServerDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GetGameServerOfUserResponse {
    private String status;
    private String message;
    private List<GameServerDTO> gameServers;
}
