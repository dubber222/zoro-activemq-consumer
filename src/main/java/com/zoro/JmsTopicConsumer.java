package com.zoro;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * Created on 2018/7/5.
 *
 * @author dubber
 */
public class JmsTopicConsumer {
    private static ConnectionFactory factory;
    private static final String brokeUrl = "tcp://192.168.116.12:61616";
    static{
        factory = new ActiveMQConnectionFactory(brokeUrl);
    }

    public static void main(String[] args) {
        new JmsTopicConsumer().process();
    }

    public void process(){
        Connection conn = null;
        try {
            conn = factory.createConnection();
            conn.start();
            // Boolean.FALSE 非事务操作，  Session.AUTO_ACKNOWLEDGE
            Session session = conn.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);

            // 创建目的地 topic（广播）
            Destination destination = session.createTopic("zoro_topic01");
            // 创建发送者
            MessageConsumer consumer = session.createConsumer(destination);
            // 创建发送的信息
            TextMessage message = (TextMessage) consumer.receive();

            System.out.println("收到的广播: " +  message.getText());

            session.close();
        } catch (JMSException e) {
            e.printStackTrace();
        }finally {
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
