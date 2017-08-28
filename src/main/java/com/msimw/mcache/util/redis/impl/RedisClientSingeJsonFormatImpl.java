package com.msimw.mcache.util.redis.impl;

import com.alibaba.fastjson.JSONObject;
import com.msimw.mcache.util.redis.api.RedisClient;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Set;

/**
 * Created by 胡明 on 17-7-17.
 *
 * redis json实现
 */
public class RedisClientSingeJsonFormatImpl implements RedisClient {

    @Autowired
    private JedisPool jedisPool;

    @Override
    public <T> T get(final String key, final Class<T> clasz) {
        if(key==null){
            return null;
        }
        return (T)new Call() {
            @Override
            protected Object call(Jedis jedis) {
                return JSONObject.parseObject(jedis.get(key),clasz);
            }
        }.run(this.jedisPool);
    }

    @Override
    public void set(final String key, final Object value) {
        if(key==null){
            return;
        }

        new Call() {
            @Override
            protected Object call(Jedis jedis) {
                return jedis.set(key,JSONObject.toJSONString(value));
            }
        }.run(this.jedisPool);
    }

    @Override
    public <T> T hget(final String hkey, final String key, final Class<T> clasz) {
        if(key==null||key==null){
            return null;
        }
        return (T)new Call() {
            @Override
            protected Object call(Jedis jedis) {
                return JSONObject.parseObject(jedis.hget(hkey,key),clasz);
            }
        }.run(this.jedisPool);
    }

    @Override
    public void hset(final String hkey, final String key, final Object value) {
        if(key==null||hkey==null){
            return;
        }

        new Call() {
            @Override
            protected Object call(Jedis jedis) {
                return jedis.hset(hkey,key,JSONObject.toJSONString(value));
            }
        }.run(this.jedisPool);
    }

    @Override
    public void expire(final String key, final int second) {
        if(key==null){
            return;
        }

        new Call() {
            @Override
            protected Object call(Jedis jedis) {
                return jedis.expire(key,second);
            }
        }.run(this.jedisPool);
    }

    @Override
    public long ttl(final String key) {
        if(key==null){
            return -1;
        }
        return (long) new Call() {
            @Override
            protected Object call(Jedis jedis) {
                return jedis.ttl(key);
            }
        }.run(this.jedisPool);
    }

    @Override
    public void del(final String key) {
        if(key==null){
            return;
        }
        new Call() {
            @Override
            protected Object call(Jedis jedis) {
                return jedis.del(key);
            }
        }.run(this.jedisPool);
    }

    @Override
    public void hdel(final String hkey, final String key) {
        if(key==null||hkey==null){
            return;
        }
        new Call() {
            @Override
            protected Object call(Jedis jedis) {
                return jedis.hdel(hkey,key);
            }
        }.run(this.jedisPool);
    }

    @Override
    public Set<String> keys(final String key) {
        if(key==null){
            return null;
        }
       return (Set<String>) new Call() {
            @Override
            protected Object call(Jedis jedis) {
                return jedis.keys(key);
            }
        }.run(this.jedisPool);
    }

    @Override
    public Set<String> hkeys(final String key) {
        if(key==null){
            return null;
        }
        return   (Set<String>) new Call() {
            @Override
            protected Object call(Jedis jedis) {
                return jedis.hkeys(key);
            }
        }.run(this.jedisPool);
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
