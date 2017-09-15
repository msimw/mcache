package com.msimw.demo.test;

import com.msimw.demo.api.IDemoService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * Created by msimw on 17-9-15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext-test.xml")
public class IDemoServiceTest {

    @Autowired
    IDemoService demoService;

    @Test
    public void query() throws Exception {
        demoService.query("1",null);
    }

    @Test
    public void update() throws Exception {
        demoService.update("1",null);
    }

}