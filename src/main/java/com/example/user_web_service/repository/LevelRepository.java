package com.example.user_web_service.repository;

import com.example.user_web_service.entity.Game;
import com.example.user_web_service.entity.Level;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface LevelRepository extends JpaRepository<Level, Long> {
    Optional<Level> findByNameAndGame(String name, Game game);
    Optional<List<Level>> findAllByGame(Game game);
}
