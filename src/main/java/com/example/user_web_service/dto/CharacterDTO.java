package com.example.user_web_service.dto;



import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CharacterDTO {
    private Long id;
    private String name;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "0.0")
    private float basicMaxHP;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "0.0")
    private float currentHP;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "0.0")
    private float basicMaxMP;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "0.0")
    private float currentMP;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "0.0")
    private float basicMaxStamina;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "0.0")
    private float currentStamina;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "0.0")
    private float basicSpeed;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "0.0")
    private float basicRecuperateHP;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "0.0")
    private float basicRecuperateMP;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "0.0")
    private float basicRecuperateStamina;
    private int freePoint;
    private LevelProgressDTO characterLevel;
    private List<CharacterAttributeDTO> attribute;
}
