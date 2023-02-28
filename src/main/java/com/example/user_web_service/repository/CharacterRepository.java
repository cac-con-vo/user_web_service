package com.example.user_web_service.repository;

import com.example.user_web_service.entity.Character;
import com.example.user_web_service.entity.CharacterStatus;
import com.example.user_web_service.entity.GameServer;
import com.example.user_web_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CharacterRepository extends JpaRepository<Character, Long> {
    boolean existsByName(String name);
    List<Character> findByUserAndGameServerAndStatusIn(User user, GameServer gameServer, List<CharacterStatus> statuses);
    Optional<Character> findByUserAndGameServerAndStatus(User user, GameServer gameServer, CharacterStatus statuses);
    boolean existsByGameServer(GameServer gameServer);
    Optional<Character> findByUserAndGameServer(User user, GameServer gameServer);

}
