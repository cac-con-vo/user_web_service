package com.example.user_web_service.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "refresh_token")
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @NotNull
    private User user;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    @NotNull
    private Instant expiryDate;


    public RefreshToken(RefreshToken refreshToken) {
        this.id = refreshToken.getId();
        this.user = refreshToken.user;
        this.token = refreshToken.getToken();
        this.expiryDate = refreshToken.getExpiryDate();
    }
}

