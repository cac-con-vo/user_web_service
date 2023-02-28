package com.example.user_web_service.repository;

import com.example.user_web_service.entity.Game;
import com.example.user_web_service.entity.GameServer;
import com.example.user_web_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GameServerRepository extends JpaRepository<GameServer, Long> {
    boolean existsByName(String name);

    Optional<GameServer> findByName(String name);

    List<GameServer> findAllByUsers(User user);
    List<GameServer> findAllByGame(Game game);
    boolean existsByUsers(User user);

}
