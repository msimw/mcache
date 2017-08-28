package com.msimw.mcache.annotation;

import java.lang.annotation.*;

/**
 * Created by 胡明 on 17-7-17.
 * 数据更新(清空)注解.多个 WITH1.7
 */
@Target(value={ElementType.METHOD})//类型上
@Retention(RetentionPolicy.RUNTIME)//作用域
@Inherited
public @interface CacheEvicts {


    CacheEvict[] value();
}
