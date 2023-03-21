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

public class AssetDTO {
    private String id;
    private String name;
    private String description;
    private int cost;
    private Long type ;
    List<AssetAttributeDTO> characterAttribute;
}
