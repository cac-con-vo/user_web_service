package com.example.user_web_service.repository;

import com.example.user_web_service.entity.AttributeGroup;
import com.example.user_web_service.entity.Character;
import com.example.user_web_service.entity.CharacterAttribute;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CharacterAttributeRepository extends JpaRepository<CharacterAttribute, Long> {
    Optional<CharacterAttribute> findByCharacterAndAttributeGroup(Character character, AttributeGroup attributeGroup);
}
