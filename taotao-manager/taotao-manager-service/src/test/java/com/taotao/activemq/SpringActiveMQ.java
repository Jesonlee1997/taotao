package com.taotao.activemq;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import javax.jms.Queue;
import javax.jms.Topic;

/**
 * Created by JesonLee
 * on 2017/4/26.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/applicationContext-activemq.xml")
public class SpringActiveMQ {

    @Autowired
    private JmsTemplate jmsTemplate;

    @Resource(name = "test-topic")
    private Topic topic;

    @Resource(name = "test-queue")
    private Queue queue;

    //使用jsmTemplate发送消息
    @Test
    public void testJmsTemplate() {
        jmsTemplate.send(queue, session -> session.createTextMessage("spring send"));
    }

}
