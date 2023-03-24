package com.example.user_web_service.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;



@Configuration
public class RedisConfig {

//    @Bean
//    public LettuceConnectionFactory redisConnectionFactory(){
//    final RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
//    configuration.setHostName("redis-11135.c263.us-east-1-2.ec2.cloud.redislabs.com");
//    configuration.setPort(11135);
//    configuration.setPassword(RedisPassword.of("DOMIHDYb5PPRBa9wyszoZEENVo9MzNF6"));
//
//    return new LettuceConnectionFactory(configuration);
//}
//    @Bean
//    public RedisTemplate<String, Object> redisTemplate(){
//        final RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
//        redisTemplate.setConnectionFactory(redisConnectionFactory());
//        return redisTemplate;
//    }


    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private String redisPort;

    @Value("${spring.redis.password}")
    private String redisPassword;

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        String redisUrl = "redis://" + redisHost + ":" + redisPort;
        config.useSingleServer()
                .setAddress(redisUrl)
                .setPassword(redisPassword);
        return Redisson.create(config);
    }

}
