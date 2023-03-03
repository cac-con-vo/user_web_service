package com.example.user_web_service.repository;

import com.example.user_web_service.entity.Game;
import com.example.user_web_service.entity.GameServer;
import com.example.user_web_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GameServerRepository extends JpaRepository<GameServer, Long> {
    boolean existsByName(String name);

    Optional<GameServer> findByNameAndGame(String name, Game game);

    List<GameServer> findAllByUsersAndGame(User user, Game game);
    List<GameServer> findAllByGame(Game game);
    boolean existsByUsers(User user);

}
