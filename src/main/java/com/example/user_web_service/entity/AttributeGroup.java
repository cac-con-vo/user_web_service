package com.example.user_web_service.entity;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@Table(name = "attribute_group")
public class AttributeGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private double value;
    @JsonIgnore
    private String effect;


    @JsonAnyGetter
    public Map<String, String> getEffects() {
        Map<String, String> effects = new HashMap<>();
        if (effect != null) {
            // Parse string "HP:+20,MP:+10" to map {"HP": "+20", "MP": "+10"}
            String[] effectArray = effect.split(",");
            for (String e : effectArray) {
                String[] parts = e.split(":");
                effects.put(parts[0], parts[1]);
            }
        }
        return effects;
    }

    @JsonAnySetter
    public void setEffects(String key, String value) {
        if (effect == null) {
            effect = "";
        }
        effect += key + ":" + value + ",";
    }
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "character_id", nullable = false, referencedColumnName = "id")
    private Character character;
}
