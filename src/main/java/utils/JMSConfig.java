package utils;


import com.ibm.msg.client.jms.JmsConnectionFactory;
import com.ibm.msg.client.jms.JmsFactoryFactory;
import com.ibm.msg.client.wmq.WMQConstants;
import com.ibm.msg.client.wmq.common.CommonConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.*;

public class JMSConfig {
    @Autowired
    ReadConfig rf;
    private String host, qMan, channel, qName, userName, password;
    private int port;

    private Queue queue;

    private JmsFactoryFactory factory;
    private JmsConnectionFactory connectionFactory;
    private JMSContext context;
    private JMSConsumer consumer;
    private JMSProducer producer;
    Destination destination;


    public static final Logger log = LoggerFactory.getLogger(JMSConfig.class);

    public JMSConfig(String host, String qMan, String channel, String qName, int port) {
        this.host = host;
        this.qMan = qMan;
        this.channel = channel;
        this.qName = qName;
        this.port = port;
        this.rf = rf;
    }


    public void init(boolean isProducer) throws JMSException {
        log.info("queue create start");

        factory = JmsFactoryFactory.getInstance(WMQConstants.WMQ_PROVIDER);
        connectionFactory = factory.createConnectionFactory();
        if (!isProducer) {
            connectionFactory.setIntProperty(CommonConstants.ACKNOWLEDGE_MODE, WMQConstants.AUTO_ACKNOWLEDGE);
        }
        connectionFactory.setStringProperty(WMQConstants.WMQ_HOST_NAME, host);
        connectionFactory.setIntProperty(WMQConstants.WMQ_PORT, port);
        connectionFactory.setStringProperty(WMQConstants.WMQ_CHANNEL, channel);
        connectionFactory.setIntProperty(WMQConstants.WMQ_CONNECTION_MODE, WMQConstants.WMQ_CM_CLIENT);
        connectionFactory.setStringProperty(WMQConstants.WMQ_QUEUE_MANAGER, qMan);
        context = null;
        if (isProducer) {
            context = connectionFactory.createContext();
            producer = context.createProducer();
            log.info("is a producer");
        } else {
            context = connectionFactory.createContext(JMSContext.AUTO_ACKNOWLEDGE);
            consumer = context.createConsumer(getQueue(), "");
        }
        log.info("Initialized Queue + " + getQueue());
    }

    public Queue getQueue() {
        return context.createQueue("queue:///" + qName);
    }

    public void putMessage(String message) {

        TextMessage msg = context.createTextMessage(message);
        destination = context.createQueue(getQueue().toString());

        producer.send(destination, msg);
    }

    public void emptyQueue() {
        Queue destination = context.createQueue(getQueue().toString());
        JMSConsumer consumer = context.createConsumer(destination);
        log.info("Clear queue starts");

        int counter = 1;
        while (consumer.receive(1000) != null) {
            if (counter % 100 == 0) {

                log.info("Clearing " + counter);
            }
            ++counter;
        }
        log.info("Clear complete");
    }
}