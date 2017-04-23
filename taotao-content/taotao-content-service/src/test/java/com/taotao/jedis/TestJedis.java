package com.taotao.jedis;

import org.junit.Test;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by JesonLee
 * on 2017/4/22.
 */
public class TestJedis {
    @Test
    public void testJedis() {
        //创建一个jedis对象，需要指定服务的Ip的端口
        Jedis jedis = new Jedis("192.168.56.152", 6379);
        //直接操作数据库
        jedis.set("jedisKey", "1234");
        String s = jedis.get("jedisKey");
        System.out.println(s);
        //关闭jedis
        jedis.close();
    }

    @Test
    public void testJedisPool() {
        //创建一个数据库连接池对象，需要指定服务的ip的端口号
        JedisPool jedisPool = new JedisPool("192.168.56.152", 6379);

        //从连接池中获得连接
        Jedis jedis = jedisPool.getResource();

        //使用Jedis操作数据库
        String s = jedis.get("jedisKey");
        System.out.println(s);

        //一定要关闭Jedis连接
        jedis.close();

        //系统关闭前关闭连接池
        jedisPool.close();

    }

    @Test
    public void testJedisCluster() {
        //创建一个JedisCluster对象，构造参数Set类型，集合中每个元素是HostAndPort类型
        Set<HostAndPort> nodes = new HashSet<HostAndPort>(){{
            add(new HostAndPort("192.168.56.152", 7001));
            add(new HostAndPort("192.168.56.152", 7002));
            add(new HostAndPort("192.168.56.152", 7003));
            add(new HostAndPort("192.168.56.152", 7004));
            add(new HostAndPort("192.168.56.152", 7005));
            add(new HostAndPort("192.168.56.152", 7006));

        }};
        JedisCluster jedisCluster = new JedisCluster(nodes);

        //直接使用JedisCluster操作redis，自带连接池，可以是单例的
        jedisCluster.set("cluster-test", "hello jedis cluster");
        String s = jedisCluster.get("cluster-test");
        System.out.println(s);

        //系统关闭前关闭JedisCluster

    }
}
