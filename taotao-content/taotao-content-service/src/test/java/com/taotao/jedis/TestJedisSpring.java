package com.taotao.jedis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by JesonLee
 * on 2017/4/23.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
        value = {"classpath:spring/applicationContext-redis.xml"})
public class TestJedisSpring {
    @Autowired
    private JedisClient jedisClient;

    @Test
    public void testJedisClientPool() {
        jedisClient.set("jedisClient", "test1");
        String s = jedisClient.get("jedisClient");
        System.out.println(s);
    }
}
