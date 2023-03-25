package com.example.user_web_service.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;


import javax.persistence.*;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@Table(name = "attribute_group")

public class AttributeGroup implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    @JsonIgnore
    private String effect;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "game_id", nullable = false, referencedColumnName = "id")
    private Game game;


    @JsonProperty("effects")
    public Map<String, Object> getEffectMap() {
        Map<String, Object> effects = new HashMap<>();
        if (effect != null) {
            String[] effectArray = effect.split(",\\s*");
            for (String e : effectArray) {
                String[] parts = e.split(":\\s*");
                effects.put(parts[0], parts[1]);
            }
        }
        return effects;
    }

    @JsonProperty("effects")
    public void setEffectMap(Map<String, Object> effects) {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, Object> entry : effects.entrySet()) {
            builder.append(entry.getKey()).append(":").append(entry.getValue()).append(",");
        }
        this.effect = builder.toString();
    }
   
}
