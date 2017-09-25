package com.msimw.mcache.util.redis.api;

import java.util.Set;

/**
 * Created by 胡明 on 17-6-20.
 *
 * redis 缓存链接工具
 */
public interface RedisClient {

    /**
     * 获取一个值
     * @param key
     * @return
     */
    <T> T get(String key, Class<T> clasz);


    /**
     * 设在一个值
     * @param key
     * @param value
     */
    void set(String key, Object value);

    /**
     * hash
     * @param hkey
     * @param key
     * @return
     */
    <T> T hget(String hkey, String key, Class<T> clasz);


    /**
     *
     * @param hkey
     * @param key
     * @param value
     */
    void hset(String hkey, String key, Object value);

    /**
     * 设置超时时间
     * @param key
     * @param second
     */
    void expire(String key, int second);

    /**
     * 查询超时时间
     * @param key
     * @return
     */
    long ttl(String key);

    /**
     *
     * @param key
     */
    void del(String key);

    /**
     *
     * @param hkey
     * @param key
     */
    void hdel(String hkey, String key);

    /**
     *
      * @param key
     * @return
     */
    public Set<String> keys(String key);


    /**
     *
     * @param key
     * @return
     */
    public Set<String> hkeys(String key);



}
