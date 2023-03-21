package com.example.user_web_service.dto;



import com.example.user_web_service.entity.CharacterAttribute;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.management.Attribute;
import java.util.List;
import java.util.Map;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CharacterDTO {
    @JsonProperty("CharacterId")
    private Long id;
    @JsonProperty("CharacterName")
    private String name;
    @JsonProperty("BasicMaxHP")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "0.0")
    private float basicMaxHP;
    @JsonProperty("CurrentHP")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "0.0")
    private float currentHP;
    @JsonProperty("BasicMaxMP")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "0.0")
    private float basicMaxMP;
    @JsonProperty("CurrentMP")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "0.0")
    private float currentMP;
    @JsonProperty("BasicMaxStamina")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "0.0")
    private float basicMaxStamina;
    @JsonProperty("CurrentStamina")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "0.0")
    private float currentStamina;
    @JsonProperty("BasicSpeed")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "0.0")
    private float basicSpeed;
    @JsonProperty("BasicRecuperateHP")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "0.0")
    private float basicRecuperateHP;
    @JsonProperty("BasicRecuperateMP")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "0.0")
    private float basicRecuperateMP;
    @JsonProperty("BasicRecuperateStamina")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "0.0")
    private float basicRecuperateStamina;

    private int freePoint;
    @JsonProperty("CharacterLevel")
    private LevelProgressDTO characterLevel;
    private List<CharacterAttributeDTO> attribute;
}
