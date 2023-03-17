package com.example.user_web_service.repository;

import com.example.user_web_service.entity.Character;
import com.example.user_web_service.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WalletRepository extends JpaRepository<Wallet, Long> {
    Wallet findByCharacter(Character character);
}
