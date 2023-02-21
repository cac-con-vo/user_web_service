//package com.example.user_web_service.service;
//
//import org.springframework.data.redis.core.ListOperations;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.stereotype.Service;
//
//import javax.annotation.PostConstruct;
//
//@Service
//public class RedisListCache {
//    private RedisTemplate<String, Object> redisTemplate;
//    private ListOperations<String, Object> listOperations;
//
//    public RedisListCache(RedisTemplate<String, Object> redisTemplate){
//        this.redisTemplate = redisTemplate;
//        listOperations = redisTemplate.opsForList();
//    }
//
//    @PostConstruct
//    public void setup(){
//        listOperations.leftPush("key", "hello there");
//
//        System.out.println(listOperations.rightPop("xx"));
//    }
//}
