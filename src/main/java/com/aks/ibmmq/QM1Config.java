package com.aks.ibmmq;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;

import com.ibm.mq.jms.MQQueueConnectionFactory;
import com.ibm.msg.client.wmq.WMQConstants;

@Configuration
public class QM1Config {
	
	@Value("${qm1.queueManager}")
	private String queueManager;
	
	@Value("${qm1.host}")
	private String host;
	
	@Value("${qm1.port}")
	private int port;
	
	@Value("${qm1.channel}")
	private String channel;
	
	@Value("${example.queue}")
	private String queue;
	
	@Bean
	  public MQQueueConnectionFactory mqQueueConnectionFactory() {
	    MQQueueConnectionFactory mqQueueConnectionFactory = new MQQueueConnectionFactory();
	    try {
	      mqQueueConnectionFactory.setHostName(host);
	      mqQueueConnectionFactory.setQueueManager(queueManager);
	      mqQueueConnectionFactory.setPort(port);
	      mqQueueConnectionFactory.setChannel(channel);
	      mqQueueConnectionFactory.setTransportType(WMQConstants.WMQ_CM_CLIENT);
	      mqQueueConnectionFactory.setCCSID(1208);
	      mqQueueConnectionFactory.setStringProperty(WMQConstants.USERID, "admin");
	      mqQueueConnectionFactory.setStringProperty(WMQConstants.PASSWORD, "passw0rd");
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	    return mqQueueConnectionFactory;
	  }
    
    @Bean
    public JmsTemplate jmsTemplate() {
        JmsTemplate template = new JmsTemplate();
        template.setConnectionFactory(mqQueueConnectionFactory());
        template.setPubSubDomain(false); // false for a Queue, true for a Topic
        template.setDefaultDestinationName(queue);
        return template;
    }
    
    @Bean(name = "jmsMQContainerFactory")
    public JmsListenerContainerFactory<?> JmsListenerContainerFactory() {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setPubSubDomain(false);
        factory.setConnectionFactory(mqQueueConnectionFactory());
        return factory;
    }
}
