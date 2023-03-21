package com.example.user_web_service.payload.response;

import com.example.user_web_service.dto.CharacterDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GetCharacterResponse {
    private String status;
    private String message;
    private CharacterDTO character;
}
