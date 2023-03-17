package com.example.user_web_service.repository;

import com.example.user_web_service.entity.WalletCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WalletCategoryRepository extends JpaRepository<WalletCategory, Long> {
    Optional<WalletCategory> findByName(String name);
}
