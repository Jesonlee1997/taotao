package com.taotao.activemq;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Test;

import javax.jms.*;
import java.io.IOException;

/**
 * Created by JesonLee
 * on 2017/4/26.
 */
public class testActiveMq {
    @Test
    public void testQueueProducer() throws JMSException {
        //1.创建一个连接工厂
        ConnectionFactory connectionFactory =
                new ActiveMQConnectionFactory("tcp://192.168.56.154:61616");

        //2.创建并开启连接
        Connection connection = connectionFactory.createConnection();
        connection.start();

        //3.使用Connection对象创建一个Session对象
        //第一个参数是是否开启事务（分布式事务，一般不使用），保证数据的最终一致，可以使用消息队列实现
        //第二个参数是消息的应答模式，自动应答，手动应答
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        //4.使用Session对象创建一个Destination对象，queue或topic
        Queue queue = session.createQueue("test-queue");

        //5.使用Session对象创建一个Producer
        MessageProducer producer = session.createProducer(queue);

        //6.创建一个TextMessage对象
        /*TextMessage textMessage = new ActiveMQTextMessage();
        message.setText("hi there");*/
        TextMessage textMessage = session.createTextMessage("hello there2");

        //8.发送消息，关闭资源
        producer.send(textMessage);
        producer.close();
        session.close();
        connection.close();

    }

    @Test
    public void testQueueConsumer() throws JMSException, InterruptedException, IOException {
        //1.创建一个连接工厂
        ConnectionFactory connectionFactory =
                new ActiveMQConnectionFactory("tcp://192.168.56.154:61616");

        //2.创建并开启连接
        Connection connection = connectionFactory.createConnection();
        connection.start();

        //3.使用Connection对象创建一个Session对象
        //第一个参数是是否开启事务（分布式事务，一般不使用），保证数据的最终一致，可以使用消息队列实现
        //第二个参数是消息的应答模式，自动应答，手动应答
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        //4.使用Session对象创建一个Destination对象，queue或topic
        Queue queue = session.createQueue("test-queue");

        MessageConsumer consumer = session.createConsumer(queue);

        consumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                //取消息的内容
                if (message instanceof TextMessage) {
                    TextMessage textMessage = (TextMessage) message;
                    try {
                        System.out.println(textMessage.getText());
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        System.in.read();
        consumer.close();
        session.close();
        connection.close();
    }

    @Test
    public void testTopicProducer() throws JMSException {
        //1.创建一个连接工厂
        ConnectionFactory connectionFactory =
                new ActiveMQConnectionFactory("tcp://192.168.56.154:61616");

        //2.创建并开启连接
        Connection connection = connectionFactory.createConnection();
        connection.start();

        //3.使用Connection对象创建一个Session对象
        //第一个参数是是否开启事务（分布式事务，一般不使用），保证数据的最终一致，可以使用消息队列实现
        //第二个参数是消息的应答模式，自动应答，手动应答
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        //4.使用Session对象创建一个Destination对象，queue或topic
        Topic topic = session.createTopic("test-topic");

        //5.使用Session对象创建一个Producer
        MessageProducer producer = session.createProducer(topic);

        //6.创建一个TextMessage对象
        /*TextMessage textMessage = new ActiveMQTextMessage();
        message.setText("hi there");*/
        TextMessage textMessage = session.createTextMessage("hello there2 topic");

        //8.发送消息，关闭资源
        producer.send(textMessage);
        producer.close();
        session.close();
        connection.close();
    }

    @Test
    public void testTopicConsumer() throws JMSException, IOException {
        //1.创建一个连接工厂
        ConnectionFactory connectionFactory =
                new ActiveMQConnectionFactory("tcp://192.168.56.154:61616");

        //2.创建并开启连接
        Connection connection = connectionFactory.createConnection();
        connection.start();

        //3.使用Connection对象创建一个Session对象
        //第一个参数是是否开启事务（分布式事务，一般不使用），保证数据的最终一致，可以使用消息队列实现
        //第二个参数是消息的应答模式，自动应答，手动应答
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        //4.使用Session对象创建一个Destination对象，queue或topic
        Topic topic = session.createTopic("test-topic");


        MessageConsumer consumer = session.createConsumer(topic);

        consumer.setMessageListener(message -> {
            //取消息的内容
            if (message instanceof TextMessage) {
                TextMessage textMessage = (TextMessage) message;
                try {
                    System.out.println(textMessage.getText());
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });
        System.out.println("topic 消费者2");
        System.in.read();
        consumer.close();
        session.close();
        connection.close();
    }
}
