package com.example.user_web_service.entity;

public enum AttributeGroupName {
    VIGOR(1),
    MIND(2),
    ENDURANCE(3),
    STRENGTH(4),
    INTELLIGENCE(5),
    FLEXIBLE(6),
    DEXTERITY(7);

    private final int value;

    AttributeGroupName(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}