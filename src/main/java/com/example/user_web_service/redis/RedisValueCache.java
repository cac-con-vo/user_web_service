package com.example.user_web_service.redis;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

@Service
public class RedisValueCache {

    private ValueOperations<String, Object> valueOperations;

    public RedisValueCache(final RedisTemplate<String, Object> redisTemplate){
        this.valueOperations = redisTemplate.opsForValue();
    }
    public void cache(final String key, final Object data){
        valueOperations.set(key, data);
    }

    public Object getCacheValue(final String key){
        return valueOperations.get(key);
    }

    public void deleteCacheValue(final String key){
        valueOperations.getOperations().delete(key);
    }
}
