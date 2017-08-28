package com.msimw.mcache.impl;

import com.msimw.mcache.api.CacheSupport;
import com.whalin.MemCached.MemCachedClient;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * Created by 胡明 on 17-7-19.
 * memcahe 缓存支持
 */
public class MemcachedCacheSupport implements CacheSupport {

    @Autowired
    private MemCachedClient memCachedClient;

    @Override
    public void put(String key, Object value) {
        this.memCachedClient.set(key,value);
    }

    @Override
    public void put(String key, Object value, int timeOut) {
       this.memCachedClient.set(key,value,new Date(System.currentTimeMillis()+(timeOut*1000)));
    }

    @Override
    public <T> T get(String key, Class<T> typeName) {
        return (T) this.memCachedClient.get(key);
    }

    @Override
    public void remove(String key) {
      this.memCachedClient.delete(key);
    }


    @Override
    @Deprecated
    public void expire(String key, int second) {
      this.memCachedClient.set(key,this.memCachedClient.get(key),new Date(System.currentTimeMillis()+(second*1000)));
    }

    @Override
    @Deprecated
    public long ttl(String key) {
        return -1l;
    }
}
