package com.example.user_web_service.entity;

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
@Table(name = "character_attribute")
public class CharacterAttribute {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private float value;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "character_id", nullable = false)
    private Character character;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "attribute_group_id", nullable = false)
    private AttributeGroup attributeGroup;

    public Map<String, Object> getAttributeGroupEffects() {
        Map<String, Object> effectsMap = new HashMap<>();
        effectsMap.put(attributeGroup.getName(), attributeGroup.getEffects());
        return effectsMap;
    }

}
