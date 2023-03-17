package com.example.user_web_service.dto;

import com.example.user_web_service.entity.AttributeGroupName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)

public class CharacterAttributeDTO {
    private int id;
    private Long pointValue;
}
