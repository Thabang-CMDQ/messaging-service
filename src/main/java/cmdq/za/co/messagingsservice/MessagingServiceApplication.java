package cmdq.za.co.messagingsservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import utils.JMSConfig;

import javax.jms.JMSException;

@SpringBootApplication
@Controller
public class MessagingServiceApplication {
    public static final Logger log = LoggerFactory.getLogger(MessagingServiceApplication.class);
    @Autowired
    static JMSConfig sendToQueue;

    public static void main(String[] args) throws JMSException {
        sendToQueue = new JMSConfig("127.0.0.1", "CMDQDEV01", "DEV.APP.SVRCONN", "DEV.QUEUE.1 ", 1414);
        sendToQueue.init(true);
        SpringApplication app = new SpringApplication(MessagingServiceApplication.class);

        app.run(args);
        int count = 1;
        while (count < 4500) {
            sendToQueue.putMessage("Test Message -> " + count);
            count++;
        }
//		sendToQueue.emptyQueue();
    }

}
