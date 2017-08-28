package com.msimw.mcache.handler.impl.memcached.bean;

import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 胡明 on 17-7-19.
 * memcached 缓存分组
 */
public class MemcachedGroup implements Serializable {

    private String groupKey;
    private List<String> keys;

    public MemcachedGroup(String groupKey, List<String> keys) {
        this.groupKey = groupKey;
        this.keys = keys;
    }

    public MemcachedGroup() {
    }

    /**
     *
     * @param key
     */
    public void addKey(String key){
        if(this.keys==null){
            this.keys =new ArrayList<>();
        }
        this.keys.add(key);
    }

    /**
     *
     * @param key
     */
    public void delKey(String key){
        if(CollectionUtils.isEmpty(this.keys)){
           return;
        }
        this.keys.remove(key);
    }


    public String getGroupKey() {
        return groupKey;
    }

    public void setGroupKey(String groupKey) {
        this.groupKey = groupKey;
    }

    public List<String> getKeys() {
        return keys;
    }

    public void setKeys(List<String> keys) {
        this.keys = keys;
    }
}
