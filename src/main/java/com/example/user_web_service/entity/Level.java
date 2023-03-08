package com.example.user_web_service.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@Table(name = "level")
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class Level implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private Long level_up_point;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "game_id", nullable = false, referencedColumnName = "id")
    private Game game;
}
