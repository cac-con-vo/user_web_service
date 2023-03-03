package com.example.user_web_service.repository;

import com.example.user_web_service.entity.Character;
import com.example.user_web_service.entity.LevelProgress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LevelProgressRepository extends JpaRepository<LevelProgress, Long> {
    Optional<LevelProgress> findFirstByCharacterOrderByLevelUpDateDesc(Character character);
}
