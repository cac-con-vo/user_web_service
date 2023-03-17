package com.example.user_web_service.repository;

import com.example.user_web_service.entity.GameServer;
import com.example.user_web_service.entity.GameServerData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GameServerDataRepository extends JpaRepository<GameServerData, Long> {
    GameServerData findByGameServer(GameServer gameServer);
}
