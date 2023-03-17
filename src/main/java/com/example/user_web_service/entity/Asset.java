package com.example.user_web_service.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@Table(name = "asset")

public class Asset implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "asset_type_id", nullable = false, referencedColumnName = "id")
    private AssetType assetType;
    private String image;
    private String description;
    private int cost;
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "character_id", nullable = false, referencedColumnName = "id")
    private Character character;

    @OneToMany(mappedBy = "asset")
    private List<AssetAttribute> assetAttributes;

}
