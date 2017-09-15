package com.msimw.demo.service;

import com.msimw.demo.api.IDemoService;
import com.msimw.demo.dto.Demo;
import com.msimw.mcache.annotation.CacheEvict;
import com.msimw.mcache.annotation.Cacheable;
import com.msimw.mcache.annotation.Cached;

/**
 * Created by msimw on 17-9-15.
 */
@Cached
public class DemoServiceImpl implements IDemoService {

    /**
     * 缓存
     */
    @Cacheable(keys = "id")
    public String query(String id, String name) {
        return "helloworld";
    }


    /**
     * 更新
     */
    @CacheEvict(keys = "id")
    public void update(String id, String name) {
    }

    /**
     * 查询
     */
    @Cacheable(keys = "id")
    @Override
    public String query1(String id) {
        return null;
    }

    /**
     * 更新
     */
    @CacheEvict(keys = "id")//or  @CacheEvict(keys = "demo.id")
    @Override
    public void update1(Demo demo) {

    }
}
