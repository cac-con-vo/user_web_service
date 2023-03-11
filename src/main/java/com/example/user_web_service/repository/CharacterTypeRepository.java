package com.example.user_web_service.repository;

import com.example.user_web_service.entity.CharacterType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface CharacterTypeRepository extends JpaRepository<CharacterType, Long> {
    Optional<CharacterType> findByName(String name);

}
