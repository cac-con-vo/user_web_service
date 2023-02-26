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
@Table(name = "game_server")
public class GameServer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private GameServerStatus status;

    private Date create_at;

    private Date update_at;

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private User user;
}
