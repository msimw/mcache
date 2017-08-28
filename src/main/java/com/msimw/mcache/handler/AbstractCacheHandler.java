package com.msimw.mcache.handler;


import com.alibaba.fastjson.JSONObject;
import com.msimw.mcache.annotation.CacheEvict;
import com.msimw.mcache.annotation.Cacheable;
import com.msimw.mcache.handler.bean.Cache;
import com.msimw.mcache.handler.bean.CacheTarget;
import com.msimw.mcache.handler.bean.Parameter;
import com.msimw.mcache.util.clasz.ClassUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by 胡明 on 17-7-14.
 * <p>
 * 统一缓存抽象类
 */
public abstract class AbstractCacheHandler implements CacheHandler {

    private static final String EMPTY_VALUE = "empty";

    private static final Log LOGGER = LogFactory.getLog(AbstractCacheHandler.class);

    private static final ExecutorService EXECUTOR_SERVICE = Executors.newCachedThreadPool();

    /**
     * 缓存处理
     *
     * @param cacheTarget
     * @return
     * @throws Throwable
     */
    @Override
    public Object handler(final CacheTarget<Cacheable> cacheTarget) throws Throwable {
        final String groupKey = generateGroupKey(cacheTarget);
        final Object cache = query(groupKey, generateKey(cacheTarget), Object.class);
        if (cache == null) {
           final Object serverResult = cacheTarget.noCache();
            /**
             * 这里我们就不等它保存了,放入执行队列
             */
            EXECUTOR_SERVICE.execute(new Runnable() {
                @Override
                public void run() {
                    Object temp = serverResult;
                    if (temp == null) { //标记空数据 防缓存击穿
                        temp = EMPTY_VALUE;
                    }
                    save(new Cache(groupKey, generateKey(cacheTarget), temp, cacheTarget.getMethTarget().getCache().survivalTime()));
                    LOGGER.debug("统一数据缓存，缓存数据到：[" + groupKey + "]分组下.");
                }
            });
            return serverResult;
        } else {
            if (EMPTY_VALUE.equals(cache)) {
                return null;
            }
            LOGGER.debug("统一数据缓存，从[" + groupKey + "]分组下获取数据.");
            return cache;
        }


    }


    /**
     * 清空缓存
     * @param cacheTarget
     * @throws Exception
     */
    @Override
    public void clear(CacheTarget<CacheEvict> cacheTarget) throws Exception {
            String groupKey = generateGroupKey(cacheTarget);
            clear(groupKey);
            LOGGER.debug("统一数据缓存,[" + groupKey + "]分组有更新，清空：[" + groupKey + "]分组下的数据.");
    }


    /**
     * 生成 缓存key
     * ，默认生成方式
     *
     * @param cacheTarget
     * @return
     */
    protected String generateGroupKey(CacheTarget cacheTarget) throws Exception {
        if (cacheTarget == null) {
            return null;
        }
        CacheTarget.MethTarget methTarget = cacheTarget.getMethTarget();

        String pro = generateProGroupKey(cacheTarget.getObjTarget(), cacheTarget.getMethTarget().getCache());
        String[] keys = (String[]) AnnotationUtils.getValue(cacheTarget.getMethTarget().getCache(), "keys");
        if (keys == null || keys.length < 1) {
            return pro;
        }

        StringBuilder builder = new StringBuilder();
        for (String key : keys) {
            builder.append(":" + getKeyValue(methTarget.getParameters(), key));
        }
        return pro + builder.toString();
    }


    /**
     * 获取key对应的值
     *
     * @param parameters
     * @param key
     * @return
     * @throws IllegalAccessException
     */
    private String getKeyValue(List<Parameter> parameters, String key) throws IllegalAccessException {
        if (CollectionUtils.isEmpty(parameters) || key == null) {
            return null;
        }
        if (key.indexOf(".") > 0) {
            String[] split = key.split("\\.");
            return pojoHandler(getParameter(parameters, split[0]), split[1]);
        }
        for (Parameter parameter : parameters) {
            if (ClassUtil.isWrapClassAndString(parameter.getType())) {
                if (key.equals(parameter.getName())) {
                    parameters.remove(parameter);
                    return String.valueOf(parameter.getValue());
                }
            } else {
                return pojoHandler(parameter, key);
            }
        }
        return null;
    }


    /**
     * 生成 key
     *
     * @param cacheTarget
     * @return
     */
    protected String generateKey(CacheTarget cacheTarget) {
        String key = cacheTarget.getObjTarget().getObj().getClass().getName()
                + "." + cacheTarget.getMethTarget().getMeth().getName() + "(" +
                buildParamsStr(cacheTarget.getMethTarget().getParameters())
                + ")";
        return DigestUtils.md5DigestAsHex(key.getBytes());
    }

    /**
     * 构建参数字符串
     *
     * @param parameters
     * @return
     */
    private String buildParamsStr(List<Parameter> parameters) {
        if (CollectionUtils.isEmpty(parameters)) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for (Parameter parameter : parameters) {
            if (ClassUtil.isWrapClassAndString(parameter.getType())) {
                //基础数据类型
                builder.append("\"" + parameter.getName() + "\":\"" + parameter.getValue() + "\",");
                continue;
            }
            builder.append("\"" + parameter.getName() + "\":" + JSONObject.toJSONString(parameter.getValue()) + ",");
        }
        return builder.toString();
    }


    /**
     * 生成前置key
     *
     * @param objTarget
     * @param cache
     * @return
     */
    private String generateProGroupKey(CacheTarget.ObjTarget objTarget, Annotation cache) {
        return objTarget.getCached().value() + ":" + AnnotationUtils.getValue(cache);
    }


    /**
     * pojo 处理
     *
     * @param parameter
     * @param key
     * @return
     * @throws IllegalAccessException
     */
    private String pojoHandler(Parameter parameter, String key) throws IllegalAccessException {
        try {
            if (key == null || parameter.getValue() == null) {
                return null;
            }
            Field field = parameter.getType().getDeclaredField(key);
            field.setAccessible(true);
            return String.valueOf(field.get(parameter.getValue()));
        } catch (NoSuchFieldException e) {
            return null;
        }
    }

    /**
     * 获取参数
     *
     * @param parameters
     * @param name
     * @return
     */
    private Parameter getParameter(List<Parameter> parameters, String name) {
        if (CollectionUtils.isEmpty(parameters) || name == null) {
            return null;
        }
        for (int i = 0; i < parameters.size(); i++) {
            Parameter parameter = parameters.get(i);
            if (parameter == null) {
                parameters.remove(null);
                continue;
            }

            if (name.equals(parameter.getName())) {
                return parameter;
            }
        }

        return null;
    }


    /**
     * 保存缓存
     *
     * @param cache
     */
    protected abstract void save(Cache cache);

    /**
     * 情况缓存
     *
     * @param groupKey
     * @param key
     * @return
     */
    protected abstract <T> T query(String groupKey, String key, Class<T> clasz);

    /**
     * 清空缓存
     *
     * @param groupKey
     */
    protected abstract void clear(String groupKey);

}
