package com.example.user_web_service.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;


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
    private String id;

    private String name;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "asset_type_id", nullable = false, referencedColumnName = "id")
    private AssetType assetType;
    private String image;
    private String description;
    private int cost;
    @ManyToMany(fetch = FetchType.EAGER)
    @JsonIgnore
    @JoinTable(name = "character_asset", joinColumns = @JoinColumn(name = "asset_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "character_id", referencedColumnName = "id"))
    private List<Character> characters;

    @OneToMany(mappedBy = "asset")
    private List<AssetAttribute> assetAttributes;

}
