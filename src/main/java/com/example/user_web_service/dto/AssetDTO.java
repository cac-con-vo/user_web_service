package com.example.user_web_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AssetDTO {
    private String id;
    private String name;
    private String description;
    private int cost;
    private Long Type ;
    List<AssetAttributeDTO> characterAttribute;
}
