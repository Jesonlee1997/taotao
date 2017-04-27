package com.taotao.solrj;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;

/**
 * Created by JesonLee
 * on 2017/4/26.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/applicationContext-activemq.xml")
public class TestSpringMQ {

    @Test
    public void testSpringActiveMq() throws IOException {
        System.in.read();
    }
}
