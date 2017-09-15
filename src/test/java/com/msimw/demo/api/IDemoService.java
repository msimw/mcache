package com.msimw.demo.api;

import com.msimw.demo.dto.Demo;

/**
 * Created by msimw on 17-9-15.
 */
public interface IDemoService {


    public String query(String id,String name);


    public void update(String id, String name);

    public String query1(String id);


    public void update1(Demo demo);
}
