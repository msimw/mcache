package com.msimw.mcache.handler.impl.memcached;


import com.msimw.mcache.handler.AbstractCacheHandler;
import com.msimw.mcache.handler.bean.Cache;
import com.msimw.mcache.handler.impl.memcached.bean.MemcachedGroup;
import com.msimw.mcache.handler.impl.memcached.util.MemCachedUtil;
import com.whalin.MemCached.MemCachedClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * Created by 胡明 on 17-7-19.
 * Memcached 缓存工具
 */
public class MemcachedHandler extends AbstractCacheHandler {

    @Autowired
    private MemCachedClient memCachedClient;

    @Override
    @Deprecated
    public void clearAll(String key) {
        Map<String, Object> keySet = MemCachedUtil.getKeySet(this.memCachedClient);
        if(CollectionUtils.isEmpty(keySet)){
            return;
        }
        Iterator<Map.Entry<String, Object>> iterator = keySet.entrySet().iterator();
        while (iterator.hasNext()){
            String key_ = iterator.next().getKey();
            if(key_.startsWith(key)){
            this.memCachedClient.delete(iterator.next().getKey());
            }
        }
    }

    @Override
    protected void save(Cache cache) {
        MemcachedGroup group = (MemcachedGroup) this.memCachedClient.get(cache.getGroup());
        if(group==null){
            group = new MemcachedGroup(cache.getGroup(),new ArrayList<String>());
        }
        group.addKey(cache.getKey());
        this.memCachedClient.set(group.getGroupKey(),group,new Date(System.currentTimeMillis()+(cache.getSurvivalTime()*1000)));
        this.memCachedClient.set(group.getGroupKey()+cache.getKey(),cache.getObject(),new Date(System.currentTimeMillis()+(cache.getSurvivalTime()*1000)));
    }


    @Override
    protected <T> T query(String groupKey, String key, Class<T> clasz) {
        return (T) memCachedClient.get(groupKey+key);
    }


    @Override
    protected void clear(String groupKey) {
        MemcachedGroup group = (MemcachedGroup) this.memCachedClient.get(groupKey);
        if(group==null){//查询分组
            return;
        }
        List<String> keys = group.getKeys();//获取分组下所有的key
        if(keys==null){
            return;
        }
        for (String key:keys){
            this.memCachedClient.delete(groupKey+key);
        }

    }
}
