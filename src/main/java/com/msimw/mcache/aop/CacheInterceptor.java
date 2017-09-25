package com.msimw.mcache.aop;

import com.msimw.mcache.annotation.CacheEvict;
import com.msimw.mcache.annotation.CacheEvicts;
import com.msimw.mcache.annotation.Cacheable;
import com.msimw.mcache.annotation.Cached;
import com.msimw.mcache.constants.CacheEnum;
import com.msimw.mcache.handler.CacheHandler;
import com.msimw.mcache.handler.bean.CacheTarget;
import com.msimw.mcache.handler.bean.Parameter;
import com.msimw.mcache.util.clasz.ClassUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.CollectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by huming on 10:13 2017/4/19.
 *
 */
public class CacheInterceptor {

    private static final Log LOGGER = LogFactory.getLog(CacheInterceptor.class);

    private CacheHandler defaultCacheHandler;

    private Map<String, CacheHandler> cacheHandlerMap;

    /**
     * 缓存数据
     *
     * @return
     */
    public Object cache(ProceedingJoinPoint jp) throws Throwable {
        try {
            Cacheable cacheable = AnnotationUtils
                    .findAnnotation(((MethodSignature) jp.getSignature())
                            .getMethod(), Cacheable.class);
           return getCacheHandler(cacheable.cache()).handler(buildCacheTarget(jp, cacheable));
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.warn("统一缓存,统一缓存代理,缓存出错,e:" + e.getClass());
        }
        return jp.proceed();
    }

    /**
     * 根据缓存更新
     */
    public void update(JoinPoint jp) throws InvocationTargetException, IllegalAccessException {
        try {
            Method method = ((MethodSignature) jp.getSignature()).getMethod();
            List<CacheEvict> cacheEvicts = getCacheEvicts(method);
            for (CacheEvict cacheEvict : cacheEvicts) {
                getCacheHandler(cacheEvict.cache()).clear(buildCacheTarget(jp, cacheEvict));
            }
        } catch (Exception e) {
            LOGGER.warn("统一缓存,统一缓存代理,缓存出错,e:" + e.getClass());
        }
    }


    /**
     * 构建缓存目标
     *
     * @param jp
     * @return
     */
    protected CacheTarget buildCacheTarget(JoinPoint jp, Annotation cacheanno) {
        if (jp == null) {
            return null;
        }
        return new CacheTarget(buildObjTarget(jp), buildMethTarget(jp, cacheanno), jp instanceof ProceedingJoinPoint ? (ProceedingJoinPoint) jp : null);


    }

    /**
     * 构建缓存 对象目标
     *
     * @param jp
     * @return
     */
    private CacheTarget.ObjTarget buildObjTarget(JoinPoint jp) {
        return new CacheTarget.ObjTarget(jp.getTarget(), AnnotationUtils.findAnnotation(jp.getTarget().getClass(), Cached.class));
    }

    /**
     * 构建缓存方法目标
     *
     * @param jp
     * @return
     */
    private CacheTarget.MethTarget buildMethTarget(JoinPoint jp, Annotation cacheanno) {
        Method method = ((MethodSignature) jp.getSignature()).getMethod();
        return new CacheTarget.MethTarget(method, cacheanno, method.getReturnType(), buildParameter(method, jp.getArgs()));

    }

    /**
     * 获取 多个CacheEvict
     *
     * @param method
     * @return
     */
    private List<CacheEvict> getCacheEvicts(Method method) {
        List<CacheEvict> cacheEvicts = new ArrayList<>();
        CacheEvict evict = AnnotationUtils.findAnnotation(method, CacheEvict.class);
        if (evict != null) cacheEvicts.add(evict);
        CacheEvicts evicts = AnnotationUtils.findAnnotation(method, CacheEvicts.class);
        if (evicts != null && evicts.value() != null) {
            for (CacheEvict cacheEvict : evicts.value()) {
                cacheEvicts.add(cacheEvict);
            }
        }
        return cacheEvicts;
    }


    /**
     * 构建方法参数目标
     *
     * @param method
     * @param args
     * @return
     */
    private List<Parameter> buildParameter(Method method, Object... args) {
        if (args == null || args.length < 1) {
            return Collections.EMPTY_LIST;
        }
        String[] paramNames = ClassUtil.getMethodParamNames(method);
        Class<?>[] parameterTypes = method.getParameterTypes();
        List<Parameter> parameters = new ArrayList<>();
        Parameter parameter = null;
        int i = 0;
        for (Object arg : args) {
            parameter = new Parameter(paramNames[i], parameterTypes[i], arg);
            parameters.add(parameter);
            i++;
        }
        return parameters;
    }


    protected CacheHandler getCacheHandler(String cacheEnum) {
        if (CollectionUtils.isEmpty(this.cacheHandlerMap)) {
            return this.defaultCacheHandler;
        }
        CacheHandler cacheHandler = this.cacheHandlerMap.get(cacheEnum);
        if (cacheHandler == null) {
            return this.defaultCacheHandler;
        }
        return cacheHandler;
    }


    public void putCacheHandler(String key, CacheHandler cacheHandler) {
        if (this.cacheHandlerMap == null) {
            this.cacheHandlerMap = new HashMap<>();
        }
        this.cacheHandlerMap.put(key, cacheHandler);
    }

    public Map<String, CacheHandler> getCacheHandlerMap() {
        return cacheHandlerMap;
    }

    public void setCacheHandlerMap(Map<String, CacheHandler> cacheHandlerMap) {
        this.cacheHandlerMap = cacheHandlerMap;
    }


    public CacheHandler getDefaultCacheHandler() {
        return defaultCacheHandler;
    }

    public void setDefaultCacheHandler(CacheHandler defaultCacheHandler) {
        this.defaultCacheHandler = defaultCacheHandler;
    }
}
