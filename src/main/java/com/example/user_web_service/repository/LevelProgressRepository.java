package com.example.user_web_service.repository;

import com.example.user_web_service.entity.Character;
import com.example.user_web_service.entity.LevelProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface LevelProgressRepository extends JpaRepository<LevelProgress, Long> {
    Optional<LevelProgress> findFirstByCharacterOrderByLevelUpDateDesc(Character character);
}
