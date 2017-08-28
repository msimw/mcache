package com.msimw.mcache.handler.bean;

import com.msimw.mcache.annotation.Cached;
import org.aspectj.lang.ProceedingJoinPoint;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by 胡明 on 17-7-14.
 *
 * 代表缓存目标
 */
public class CacheTarget<T extends Annotation> {

    /**
     * 代表目标对象
     */
    private ObjTarget objTarget;

    /**
     * 代表目标方法
     */
    private MethTarget<T> methTarget;


    /**
     *
     */
    protected ProceedingJoinPoint pjp;

    public CacheTarget() {
    }

    public CacheTarget(ObjTarget objTarget, MethTarget<T> methTarget,ProceedingJoinPoint pjp) {
        this.objTarget = objTarget;
        this.methTarget = methTarget;
        this.pjp =pjp;
    }

    /**
     * 目标对象
     */
    public static class ObjTarget{
         private Object obj;
         private Cached cached;

        public ObjTarget() {
        }

        public ObjTarget(Object obj, Cached cached) {
            this.obj = obj;
            this.cached = cached;
        }

        public Object getObj() {
            return obj;
        }

        public void setObj(Object obj) {
            this.obj = obj;
        }

        public Cached getCached() {
            return cached;
        }

        public void setCached(Cached cached) {
            this.cached = cached;
        }
    }


    /**
     * 目标方法对象
     */
    public static class MethTarget<T extends Annotation>{
        private Method meth;
        private T cache;

        private Class<?> returnType;
        private List<Parameter> parameters;

        public MethTarget() {
        }

        public MethTarget(Method meth,T cacheanno, Class<?> returnType, List<Parameter> parameters) {
            this.meth = meth;
            this.cache = cacheanno;
            this.returnType = returnType;
            this.parameters = parameters;
        }

        public Method getMeth() {
            return meth;
        }

        public void setMeth(Method meth) {
            this.meth = meth;
        }

        public T getCache() {
            return cache;
        }

        public void setCache(T cache) {
            this.cache = cache;
        }

        public Class<?> getReturnType() {
            return returnType;
        }

        public void setReturnType(Class<?> returnType) {
            this.returnType = returnType;
        }

        public List<Parameter> getParameters() {
            return parameters;
        }

        public void setParameters(List<Parameter> parameters) {
            this.parameters = parameters;
        }
    }

    public ObjTarget getObjTarget() {
        return objTarget;
    }

    public void setObjTarget(ObjTarget objTarget) {
        this.objTarget = objTarget;
    }

    public MethTarget<T> getMethTarget() {
        return methTarget;
    }

    public void setMethTarget(MethTarget<T> methTarget) {
        this.methTarget = methTarget;
    }

    /**
     * 没有缓存
     * @return
     */
    public Object noCache() throws Throwable {
          return  pjp.proceed();
    }


    public ProceedingJoinPoint getPjp() {
        return pjp;
    }

    public void setPjp(ProceedingJoinPoint pjp) {
        this.pjp = pjp;
    }
}
