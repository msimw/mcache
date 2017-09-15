package com.msimw.demo.service;

import com.msimw.demo.api.IDemoService;
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
     * @param id
     * @param name
     * @return
     */
    @Cacheable(keys = "id")
    public String query(String id, String name) {
        return "helloworld";
    }


    /**
     * 更新
     * @param id
     * @param name
     * @return
     */
    @CacheEvict(keys = "id")
    public void update(String id, String name) {
    }
}
