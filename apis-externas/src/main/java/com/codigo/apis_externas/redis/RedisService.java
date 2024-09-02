package com.codigo.apis_externas.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisService {
    private final StringRedisTemplate redisTemplate;

    public void guardarEnRedis(String key, String value, int exp){
        redisTemplate.opsForValue().set(key,value);
        redisTemplate.expire(key,exp, TimeUnit.MINUTES);
    }

    public String getDataDesdeRedis(String key){
        return redisTemplate.opsForValue().get(key);
    }

    public void borrarData(String key){
        redisTemplate.delete(key);
    }
}
