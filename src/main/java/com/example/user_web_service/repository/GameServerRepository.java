package com.example.user_web_service.repository;

import com.example.user_web_service.entity.Game;
import com.example.user_web_service.entity.GameServer;
import com.example.user_web_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface GameServerRepository extends JpaRepository<GameServer, Long> {
    boolean existsByNameAndGame(String name, Game game);

    Optional<GameServer> findByNameAndGame(String name, Game game);

    List<GameServer> findAllByUsersAndGame(User user, Game game);
    List<GameServer> findAllByGame(Game game);
    boolean existsByUsers(User user);

}
