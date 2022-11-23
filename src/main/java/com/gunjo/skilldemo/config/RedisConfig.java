package com.gunjo.skilldemo.config;

import org.springframework.context.annotation.*;
import org.springframework.core.io.*;
import org.springframework.data.redis.connection.*;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.core.script.*;
import org.springframework.data.redis.serializer.*;

/**
 * Redis配置类
 * 实现序列化
 */
@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate (RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        // key序列化
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        // value序列化
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        // hash类型 key序列化
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        // hash类型 value序列化
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());

        // 注入连接工厂
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        return redisTemplate;
    }

//    @Bean
//    public DefaultRedisScript<Boolean> script() {
//        DefaultRedisScript<Boolean> redisScript = new DefaultRedisScript<>();
//        // lock.lua脚本位置与application.yml同级目录
//        redisScript.setLocation(new ClassPathResource("lock.lua"));
//        redisScript.setResultType(Boolean.class);
//        return redisScript;
//    }

    @Bean
    public DefaultRedisScript<Long> script() {
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        // lock.lua脚本位置与application.yml同级目录
        redisScript.setLocation(new ClassPathResource("stock.lua"));
        redisScript.setResultType(Long.class);
        return redisScript;
    }

}
