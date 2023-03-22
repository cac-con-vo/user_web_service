package com.example.user_web_service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GameDataDTO {
    private float x;
    private float y;
    private float z;
    private Long pcharS;
    private String pnameS;
    private CharacterDTO characterS;
    private Long goldS;
    private List<String> weaponS;
}

