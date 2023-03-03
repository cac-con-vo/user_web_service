package com.example.user_web_service.dto;

import com.example.user_web_service.entity.*;
import com.example.user_web_service.helper.DateTimeUtils;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CharacterDTO {
    private Long id;

    private String name;

    private CharacterStatus status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateTimeUtils.DATETIME_FORMAT)
    private Date create_at;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateTimeUtils.DATETIME_FORMAT)
    private Date update_at;

    private CharacterPosition position;

    private CharacterType characterType;

    private List<CharacterAttribute> characterAttributes;

    private LevelProgress currentLevel;

    private List<Asset> assets;
}
