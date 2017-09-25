package com.msimw.mcache.handler;


import com.msimw.mcache.annotation.CacheEvict;
import com.msimw.mcache.annotation.Cacheable;
import com.msimw.mcache.handler.bean.CacheTarget;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by huming on 10:23 2017/4/19.
 *
 */
public interface CacheHandler {


    /**
     * 缓存
     * @param cacheTarget
     * @return
     * @throws Throwable
     */
    public Object handler(CacheTarget<Cacheable> cacheTarget) throws Throwable;


    /**
     * 清空缓存
     * @param cacheTarget
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public void clear(CacheTarget<CacheEvict> cacheTarget) throws InvocationTargetException, IllegalAccessException, Exception;


    /**
     * 清空缓存
     * @param key
     */
    public void clearAll(String key);
}
