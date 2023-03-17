package com.example.user_web_service.repository;


import com.example.user_web_service.entity.Asset;
import com.example.user_web_service.entity.AssetType;
import com.example.user_web_service.entity.Character;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssetRepository extends JpaRepository<Asset, Long> {
    List<Asset> findAllByCharacterAndAndAssetType(Character character, AssetType assetType);
}
