package com.example.user_web_service.payload.response;

import com.example.user_web_service.dto.AttributeEffectDTO;
import com.example.user_web_service.dto.LevelDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GetAllLevelOfGameResponse {
    private String status;
    private String message;
    private List<LevelDTO> levels;
}
