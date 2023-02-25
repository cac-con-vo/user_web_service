package com.example.user_web_service.repository;

import com.example.user_web_service.entity.GameToken;
import com.example.user_web_service.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

public interface GameTokenRepository extends JpaRepository<GameToken, Long> {
    Optional<GameToken> findByToken(String token);

    @Query("select r from GameToken r where r.user.id = ?1")
    Optional<GameToken> findByUser_Id(Long userId);

    @Query("delete from GameToken r where r.user.id = ?1")
    @Modifying
    @Transactional
    void deleteAllByUser_Id(Long id);

    @Transactional
    @Modifying
    @Query("delete from GameToken r where r.token = ?1")
    void deleteByToken(String token);

    @Transactional
    @Modifying
    @Query("delete from GameToken r where r.expiryDate <= ?1")
    void deleteAllByExpiryDateBefore(Instant now);
}
