package com.msimw.mcache.annotation;

import java.lang.annotation.*;

/**
 * Created by huming on 10:17 2017/4/19.
 *
 */
@Target(value={ElementType.TYPE})//类型上
@Retention(RetentionPolicy.RUNTIME)//作用域
@Inherited
public @interface Cached {

     /**
      * 分组名称
      * @return
      */
     String value() default "cache";
}
