package com.zoro;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * Created on 2018/7/5.
 *
 * @author dubber
 *
 * 订阅持久化
 */
public class JmsPersistenceTopicConsumer {
    private static ConnectionFactory factory;
    private static final String brokeUrl = "tcp://192.168.116.12:61616";
    private static final String CLIENT_ID = "zoro-1";
    static{
        factory = new ActiveMQConnectionFactory(brokeUrl);
    }

    public static void main(String[] args) {
        new JmsPersistenceTopicConsumer().process();
    }

    public void process(){
        Connection conn = null;
        try {
            conn = factory.createConnection();
            conn.setClientID(JmsPersistenceTopicConsumer.CLIENT_ID);
            conn.start();
            // Boolean.FALSE 非事务操作，  Session.AUTO_ACKNOWLEDGE
            Session session = conn.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);

            // 创建目的地 topic（广播）
            Topic destination = session.createTopic("zoro_topic01");

            // 创建持久化接收者
            MessageConsumer consumer = session.createDurableSubscriber(destination,JmsPersistenceTopicConsumer.CLIENT_ID);

            TextMessage message = (TextMessage) consumer.receive();

            System.out.println("收到的广播: " +  message.getText());

            //session.commit();
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
