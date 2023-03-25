package com.example.user_web_service.repository;

import com.example.user_web_service.entity.AttributeGroup;
import com.example.user_web_service.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AttributeGroupRepository extends JpaRepository<AttributeGroup, Long> {
    Optional<List<AttributeGroup>> findByGame(Game game);

}
