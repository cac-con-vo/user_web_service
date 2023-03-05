package com.example.user_web_service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.HashMap;
import java.util.Map;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AttributeEffectDTO {
    private int id;
    @JsonIgnore
    private String effect;
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
