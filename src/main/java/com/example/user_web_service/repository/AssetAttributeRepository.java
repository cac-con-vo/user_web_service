package com.example.user_web_service.repository;

import com.example.user_web_service.entity.Asset;
import com.example.user_web_service.entity.AssetAttribute;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssetAttributeRepository extends JpaRepository<AssetAttribute, Long> {
    List<AssetAttribute> findAllByAsset(Asset asset);
}
