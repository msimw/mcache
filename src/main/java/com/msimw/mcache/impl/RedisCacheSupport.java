package com.msimw.mcache.impl;


import com.msimw.mcache.api.CacheSupport;
import com.msimw.mcache.util.redis.api.RedisClient;

/**
 * Created by 胡明 on 17-6-20.
 * redis 缓存实现
 */
public class RedisCacheSupport implements CacheSupport {

    RedisClient redisClient;

    @Override
    public void put(String key, Object value) {
        redisClient.set(key,value);
    }

    @Override
    public void put(String key, Object value, int timeOut) {
          put(key,value);
          expire(key,timeOut);
    }

    @Override
    public <T> T get(String key, Class<T> typeName) {
        return redisClient.get(key,typeName);
    }

    @Override
    public void remove(String key) {
      redisClient.del(key);
    }

    @Override
    public void expire(String key, int second) {
      redisClient.expire(key,second);
    }

    @Override
    public long ttl(String key) {
        return redisClient.ttl(key);
    }

    public RedisClient getRedisClient() {
        return redisClient;
    }

    public void setRedisClient(RedisClient redisClient) {
        this.redisClient = redisClient;
    }
}
