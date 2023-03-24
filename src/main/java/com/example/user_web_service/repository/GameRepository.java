package com.example.user_web_service.repository;


import com.example.user_web_service.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface GameRepository extends JpaRepository<Game, Long> {
    boolean existsByName(String name);
    Optional<Game> findByName(String name);
}
