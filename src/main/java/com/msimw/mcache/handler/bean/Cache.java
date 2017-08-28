package com.msimw.mcache.handler.bean;

/**
 * Created by 胡明 on 17-7-14.
 *
 * 代表缓存对象
 */
public class Cache {



    /**
     * 缓存的key
     */
    private String key;

    /**
     * 分组
     */
    private String group;

    /**
     * 被缓存的数据
     */
    private Object object;

    /**
     * 存活时间
      */
    private Integer survivalTime;

    public Cache() {
    }

    public Cache(String group,String key, Object object, Integer survivalTime) {
        this.group = group;
        this.key = key;
        this.object = object;
        this.survivalTime = survivalTime;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public Integer getSurvivalTime() {
        return survivalTime;
    }

    public void setSurvivalTime(Integer survivalTime) {
        this.survivalTime = survivalTime;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}
