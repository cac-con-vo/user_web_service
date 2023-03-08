package com.example.user_web_service.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;


@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter

public class CharacterPosition implements Serializable {
    private double x;
    private double y;
    private double z;
}
