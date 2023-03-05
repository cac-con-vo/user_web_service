package com.example.user_web_service.dto;

import com.example.user_web_service.entity.AttributeGroupName;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CharacterAttributeDTO {
    public int id;
    public Long pointValue;
}