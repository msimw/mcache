package com.msimw.mcache.annotation;


import com.msimw.mcache.constants.CacheEnum;

import java.lang.annotation.*;

/**
 * Created by 胡明 on 17-7-17.
 *
 * 数据缓存注解
 */
@Target(value={ElementType.METHOD})//类型上
@Retention(RetentionPolicy.RUNTIME)//作用域
@Inherited
public @interface Cacheable {

    /**
     * 使用哪一种缓存
     * @return
     */
    String cache() default CacheEnum.REDIS;

    /**
     * 分组名称
     * @return
     */
    String value() default "cache";

    /**
     * 缓存key 这是是参考 spring mcache  == com.bst.zw.common.mcache.annotation.CacheGroupKey（这个已摈弃）
     * 1.现在这种方式在方法上一眼就能看出，key是由那几个字段组成。
     * @return
     */
    String[] keys() default {};

    /**
     * 存活时间
     * @return
     */
    int survivalTime() default 30*60;//默认30分钟 -1表示永不过期

}
