package com.example.user_web_service.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

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

    @Column(nullable = false)
    private String name;

    private CharacterStatus status;

    private Date create_at;
    private Date update_at;


    @ManyToOne
    @JoinColumn(name = "game_server_id", nullable = false)
    private GameServer gameServer;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @ManyToOne
    @JoinColumn(name = "character_type_id", nullable = false)
    private CharacterType characterType;
}