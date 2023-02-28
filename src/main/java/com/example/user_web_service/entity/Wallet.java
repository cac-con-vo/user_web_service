package com.example.user_web_service.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@Table(name = "wallet")
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Long totalMoney;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "wallet_category_id", nullable = false, referencedColumnName = "id")
    private Game game;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "character_id", nullable = false, referencedColumnName = "id")
    private Character character;
}
