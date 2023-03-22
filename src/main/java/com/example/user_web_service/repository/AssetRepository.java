package com.example.user_web_service.repository;


import com.example.user_web_service.entity.Asset;
import com.example.user_web_service.entity.AssetType;
import com.example.user_web_service.entity.Character;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AssetRepository extends JpaRepository<Asset, String> {
    List<Asset> findAllByCharactersAndAssetType(Character character, AssetType assetType);

}
