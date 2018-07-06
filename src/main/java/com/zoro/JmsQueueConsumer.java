package com.zoro;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.IOException;

/**
 * Created on 2018/7/5.
 *
 * @author dubber
 */
public class JmsQueueConsumer {
    private static ConnectionFactory factory;
    private static final String brokeUrl = "tcp://192.168.116.12:61616";
    static{
        factory = new ActiveMQConnectionFactory(brokeUrl);
    }

    public static void main(String[] args) {
        new JmsQueueConsumer().process();
    }

    public void process(){
        Connection conn = null;
        try {
            conn = factory.createConnection();
            conn.start();
            // Boolean.FALSE 非事务操作，  Session.AUTO_ACKNOWLEDGE
            Session session = conn.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);

            // 创建目的地 queue（队列）
            Destination destination = session.createQueue("zoro_Queue01");
            // 创建消费者
            MessageConsumer consumer = session.createConsumer(destination);
            // 创建监听器
            MessageListener listener = new MessageListener() {
                @Override
                public void onMessage(Message message) {
                    try {
                        System.out.println("收到的消息: " +  ((TextMessage)message).getText());
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
            };
            consumer.setMessageListener(listener);

            System.in.read();
            session.close();
        } catch (JMSException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(conn != null){
                try {
                    conn.close();
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
