package com.msimw.mcache.annotation;


import com.msimw.mcache.constants.CacheEnum;

import java.lang.annotation.*;

/**
 * Created by 胡明 on 17-7-17.
 * 数据更新(清空)注解
 */
@Target(value={ElementType.METHOD})//类型上
@Retention(RetentionPolicy.RUNTIME)//作用域
@Inherited
public @interface CacheEvict {


    /**
     * 使用哪一种缓存
     * @return
     */
    String cache() default CacheEnum.REDIS;

    /**
     * 分组名称
     * @return
     */
    String value() default "bst";

    /**
     * 缓存key
     * @return
     */
    String[] keys() default {};
}
