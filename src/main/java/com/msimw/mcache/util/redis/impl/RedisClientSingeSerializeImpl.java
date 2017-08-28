package com.msimw.mcache.util.redis.impl;

import com.msimw.mcache.util.redis.api.RedisClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.SerializationUtils;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Set;

/**
 * Created by 胡明 on 17-6-20.
 *
 * redis 单机序列化实现
 */
public class RedisClientSingeSerializeImpl implements RedisClient {

    @Autowired
    private JedisPool jedisPool;

    @Override
    public <T> T get(final String key, Class<T> clasz) {
        if(StringUtils.isEmpty(key)){
            return null;
        }
        return (T)new Call() {
            @Override
            protected Object call(Jedis jedis) {
               return SerializationUtils.deserialize(jedis.get(key.getBytes()));
            }
        }.run(jedisPool);
    }

    @Override
    public void set(final String key, final Object value) {
        if(StringUtils.isEmpty(key)||value==null){
            return;
        }
        new Call() {
            @Override
            protected Object call(Jedis jedis) {
                return jedis.set(key.getBytes(),SerializationUtils.serialize(value));
            }
        }.run(jedisPool);
    }

    @Override
    public <T> T hget(final String hkey, final String key, Class<T> clasz) {
        if(StringUtils.isEmpty(hkey)||StringUtils.isEmpty(key)||clasz==null){
        return null;
        }

       return (T) new Call() {
            @Override
            protected Object call(Jedis jedis) {
                return SerializationUtils.deserialize(jedis.hget(hkey.getBytes(),key.getBytes()));
            }
        }.run(jedisPool);
    }

    @Override
    public void hset(final String hkey, final String key, final Object value) {
        if(StringUtils.isEmpty(hkey)||StringUtils.isEmpty(key)||value==null){
            return;
        }
        new Call() {
            @Override
            protected Object call(Jedis jedis) {
                return jedis.hset(hkey.getBytes(),key.getBytes(),SerializationUtils.serialize(value));
            }
        }.run(jedisPool);
    }

    @Override
    public void expire(final String key, final int second) {
        if(StringUtils.isEmpty(key)){
            return;
        }
        new Call() {
            @Override
            protected Object call(Jedis jedis) {
                return jedis.expire(key,second);
            }
        }.run(jedisPool);
    }

    @Override
    public long ttl(final String key) {
        if(StringUtils.isEmpty(key)){
            return -1;
        }
        return (long) new Call() {
            @Override
            protected Object call(Jedis jedis) {
                return jedis.ttl(key);
            }
        }.run(jedisPool);
    }

    @Override
    public void del(final String key) {
        if(StringUtils.isEmpty(key)){
            return;
        }
        new Call() {
            @Override
            protected Object call(Jedis jedis) {
                return jedis.del(key);
            }
        }.run(jedisPool);
    }

    @Override
    public void hdel(final String hkey, final String key) {
        if(StringUtils.isEmpty(hkey)||StringUtils.isEmpty(key)){
            return;
        }
        new Call() {
            @Override
            protected Object call(Jedis jedis) {
                return jedis.hdel(hkey,key);
            }
        }.run(jedisPool);
    }

    @Override
    public Set<String> keys(final String key) {
        if(StringUtils.isEmpty(key)){
            return null;
        }
        return (Set<String>) new Call() {
            @Override
            protected Object call(Jedis jedis) {
                return jedis.keys(key);
            }
        }.run(jedisPool);
    }

    @Override
    public Set<String> hkeys(final String key) {
        if(StringUtils.isEmpty(key)){
            return null;
        }
        return (Set<String>) new Call() {
            @Override
            protected Object call(Jedis jedis) {
                return jedis.hkeys(key);
            }
        }.run(jedisPool);
    }



    /**
     * jdeis call
     */
    private abstract class Call{

        public Object run(JedisPool jedisPool){
            Jedis jedis = null;
            try {
                jedis = jedisPool.getResource();
                return call(jedis);
            }finally {
                if(jedis!=null){
                    jedis.close();
                }
            }
        }

        protected abstract Object call(Jedis jedis);
    }

    public JedisPool getJedisPool() {
        return jedisPool;
    }

    public void setJedisPool(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }
}
