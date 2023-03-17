package com.example.user_web_service.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@Entity
@Table(name = "game_server_data")
public class GameServerData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String jsonDataSharing;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "game_server_id", nullable = false)
    private GameServer gameServer;
}
