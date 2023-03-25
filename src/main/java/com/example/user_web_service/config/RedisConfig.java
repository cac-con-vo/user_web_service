//package com.example.user_web_service.config;
//
//import org.redisson.Redisson;
//import org.redisson.api.RedissonClient;
//import org.redisson.config.Config;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//
//
//
//@Configuration
//public class RedisConfig {
//
//    @Value("${spring.redis.host}")
//    private String redisHost;
//
//    @Value("${spring.redis.port}")
//    private String redisPort;
//
//    @Value("${spring.redis.password}")
//    private String redisPassword;
//
//    @Bean
//    public RedissonClient redissonClient() {
//        Config config = new Config();
//        String redisUrl = "redis://" + redisHost + ":" + redisPort;
//        config.useSingleServer()
//                .setAddress(redisUrl)
//                .setPassword(redisPassword);
//        return Redisson.create(config);
//    }
//
//}
