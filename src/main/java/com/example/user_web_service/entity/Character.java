package com.example.user_web_service.entity;

import com.example.user_web_service.helper.DateTimeUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@Table(name = "character")
public class Character {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    //basic data
    private float basicMaxHP;
    private float currentHP;
    private float basicMaxMP;
    private float currentMP;
    private float basicMaxStamina;
    private float currentStamina;
    private float basicSpeed;
    private float basicRecuperateHP;
    private float basicRecuperateMP;
    private float basicRecuperateStamina;
    // setter method cho basicCurrentHP
    public void setCurrentHP(float value) {
        if (value < 0.0f) {
            currentHP = 0.0f;
        } else if (value > 1.0f) {
            currentHP = 1.0f;
        } else {
            currentHP = value;
        }
    }
    public void setCurrentMP(float value) {
        if (value < 0.0f) {
            currentMP = 0.0f;
        } else if (value > 1.0f) {
            currentMP = 1.0f;
        } else {
            currentMP = value;
        }
    }
    public void setCurrentStamina(float value) {
        if (value < 0.0f) {
            currentStamina = 0.0f;
        } else if (value > 1.0f) {
            currentStamina = 1.0f;
        } else {
            currentStamina = value;
        }
    }

    private int free_point;
    private CharacterStatus status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateTimeUtils.DATETIME_FORMAT)
    private Date create_at;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateTimeUtils.DATETIME_FORMAT)
    private Date update_at;


    @Embedded
    private CharacterPosition position;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "game_server_id", nullable = false)
    private GameServer gameServer;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "character_type_id", nullable = false)
    private CharacterType characterType;

    @OneToMany(mappedBy = "character")
    private List<CharacterAttribute> characterAttributes;

    @OneToMany(mappedBy = "character")
    private List<LevelProgress> levels;

    @OneToMany(mappedBy = "character")
    private List<Asset> assets;
}