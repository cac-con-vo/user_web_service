package com.example.user_web_service.entity;

import com.example.user_web_service.helper.Constant;
import com.example.user_web_service.helper.DateTimeUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

import static com.example.user_web_service.helper.Constant.DATE_FORMAT;

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
    private List<AttributeGroup> attributeGroups;

}