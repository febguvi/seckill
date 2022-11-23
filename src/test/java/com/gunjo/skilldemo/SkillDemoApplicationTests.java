package com.gunjo.skilldemo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.core.script.*;

import java.util.*;
import java.util.concurrent.*;

@SpringBootTest
class SkillDemoApplicationTests {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private RedisScript<Boolean> script;

    @Test
    void contextLoads() {

    }

    @Test
    public void testLock01() {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        // 占位 如果key不存在才能设置成功
        Boolean isLock = valueOperations.setIfAbsent("k1", "v1");
        if (isLock) {
            valueOperations.set("name", "xxxx");
            String name = (String) valueOperations.get("name");
            System.out.println("name:" + name);
            // 如果有异常则删不了锁 第一个线程就会死在里面
            Integer.parseInt("x");
            // 操作结束 删除锁
            redisTemplate.delete("k1");
        } else {
            System.out.println("有线程在使用，请稍后再试...");
        }
    }

    @Test
    public void testLock02() {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        // 设置超时时间 防止应用在运行过程中抛出异常导致锁无法释放
        Boolean isLock = valueOperations.setIfAbsent("k2", "v2", 5, TimeUnit.SECONDS);
        if (isLock) {
            valueOperations.set("name", "xxxx");
            String name = (String) valueOperations.get("name");
            System.out.println("name:" + name);
            // 如果有异常则删不了锁 第一个线程就会死在里面 第二个线程能拿到锁但是会抛异常
            Integer.parseInt("x");
            // 操作结束 删除锁
            redisTemplate.delete("k1");
        } else {
            System.out.println("有线程在使用，请稍后再试...");
        }
    }

    @Test
    public void testLock03() {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        String value = UUID.randomUUID().toString();
        Boolean isLock = valueOperations.setIfAbsent("k2", value, 120, TimeUnit.SECONDS);
        if (isLock) {
            valueOperations.set("name", "xxxx");
            String name = (String) valueOperations.get("name");
            System.out.println("name:" + name);
            System.out.println(valueOperations.get("k1"));
            Boolean result = (Boolean) redisTemplate.execute(script, Collections.singletonList("k1"), value);
            System.out.println(result);
        } else {
            System.out.println("有线程在使用，请稍后再试...");
        }
    }
}
