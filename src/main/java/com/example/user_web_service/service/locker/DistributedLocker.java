package com.example.user_web_service.service.locker;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.core.ValueOperations;
//
//
//
//public class DistributedLocker {
//    private static final Logger LOG = LoggerFactory.getLogger(DistributedLocker.class);
//    private static final long DEFAULT_RETRY_TIME =100L;
//    private final ValueOperations<String, String> valueOperations;
//
//    public DistributedLocker(final RedisTemplate<String, String> redisTemplate) {
//        this.valueOperations = redisTemplate.opsForValue();
//    }
//
//
//    public <T> LockExecutionResult<T> lock()
//}
