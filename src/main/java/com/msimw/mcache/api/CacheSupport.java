package com.msimw.mcache.api;

/**
 * Created by 胡明 on 17-6-20.
 * 数据缓存超级接口
 */
public interface CacheSupport {

    int DEFAULT_TIME_OUT=1800; //second

    public void put(String key, Object value);

    public void put(String key, Object value, int timeOut);

    public <T> T get(String key, Class<T> typeName);

    public void remove(String key);

    public void expire(String key, int second);

    public long ttl(String key);
}
