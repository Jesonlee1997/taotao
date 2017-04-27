package com.taotao.search.listener;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * Created by JesonLee
 * on 2017/4/26.
 */
public class MyMessageListener implements MessageListener {

    @Override
    public void onMessage(Message message) {
        //接收到消息
        TextMessage textMessage = (TextMessage) message;
        try {
            System.out.println(textMessage.getText());
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
