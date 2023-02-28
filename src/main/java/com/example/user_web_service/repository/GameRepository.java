package com.example.user_web_service.repository;


import com.example.user_web_service.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GameRepository extends JpaRepository<Game, Long> {
    boolean existsByName(String name);
    Game findByName(String name);
}
