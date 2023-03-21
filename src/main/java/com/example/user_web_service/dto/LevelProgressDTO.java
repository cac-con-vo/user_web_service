package com.example.user_web_service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class LevelProgressDTO {
    @JsonProperty("CurrentLevel")
    private int currentLevel;
    @JsonProperty("LevelPoint")
    private long levelPoint;
}
