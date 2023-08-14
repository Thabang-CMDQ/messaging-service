package utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
public class ReadConfig {
    private int port;
    private String qMan;
    @Value("${ibm.mq.channel}")
    private String channel;
    private String host;

    private String entryQueue;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getqMan() {
        return qMan;
    }

    public void setqMan(String qMan) {
        this.qMan = qMan;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getEntryQueue() {
        return entryQueue;
    }

    public void setEntryQueue(String entryQueue) {
        this.entryQueue = entryQueue;
    }


}
