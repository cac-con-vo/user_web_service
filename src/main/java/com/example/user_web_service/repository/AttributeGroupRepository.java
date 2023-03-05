package com.example.user_web_service.repository;

import com.example.user_web_service.entity.AttributeGroup;
import com.example.user_web_service.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface AttributeGroupRepository extends JpaRepository<AttributeGroup, Long> {
    Optional<List<AttributeGroup>> findByGame(Game game);

}
