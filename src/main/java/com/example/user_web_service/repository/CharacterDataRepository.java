package com.example.user_web_service.repository;

import com.example.user_web_service.entity.Character;
import com.example.user_web_service.entity.CharacterData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CharacterDataRepository extends JpaRepository<CharacterData, Long> {
    CharacterData findByCharacter(Character character);
}
