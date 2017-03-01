package com.ex;


import com.alibaba.fastjson.JSONObject;
import com.ex.dao.CommentDAO;
import com.ex.dao.LoginTicketDAO;
import com.ex.dao.UserDAO;
import com.ex.model.*;
import com.ex.service.NewsService;
import com.ex.service.UserService;
import com.ex.util.JedisAdapter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.Random;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = DemoApplication.class)

public class RedisTests {

    @Autowired
    JedisAdapter jedisAdapter;

    @Test
    public void testRedis(){
        User user=new User();
        user.setName("xc");
        user.setPassword("123");
        user.setHeadUrl("wwww");
        user.setSalt("salt");

        jedisAdapter.set("user", JSONObject.toJSONString(user));

        System.out.println(jedisAdapter.getObject("user",User.class));

    }


}
