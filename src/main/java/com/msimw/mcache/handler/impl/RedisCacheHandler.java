package com.msimw.mcache.handler.impl;

import com.msimw.mcache.handler.AbstractCacheHandler;
import com.msimw.mcache.handler.bean.Cache;
import com.msimw.mcache.util.redis.api.RedisClient;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.Set;

/**
 * Created by huming on 10:26 2017/4/19.
 *
 * @Author: huming
 * @Description: redis CacheHandler
 * 1.通过spring aop实现缓存.
 * 2.会缓存@Cached的类的查询方法.修改时,针对统一分组清空
 * @Modified By:
 */
public class RedisCacheHandler extends AbstractCacheHandler {

    private static final Log LOGGER = LogFactory.getLog(RedisCacheHandler.class);
    @Autowired
    private RedisClient redisClient;


    @Override
    public void clearAll(String key) {
        Set<String> keys = redisClient.keys(key + "*");
        if (CollectionUtils.isEmpty(keys)) {
            return;
        }
        for (String k : keys) {
            redisClient.del(k);
        }

    }

    @Override
    protected void save(Cache cache) {
        redisClient.hset(cache.getGroup(), cache.getKey(), cache.getObject());
        if(cache.getSurvivalTime()!=-1){
        redisClient.expire(cache.getGroup(), cache.getSurvivalTime());
        }
    }

    @Override
    protected <T> T query(String groupKey, String key, Class<T> clasz) {
        return redisClient.hget(groupKey, key, clasz);
    }

    @Override
    protected void clear(String groupKey) {
        redisClient.del(groupKey);
    }
}
