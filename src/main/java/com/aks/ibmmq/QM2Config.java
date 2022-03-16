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
public class QM2Config {

	@Value("${qm2.queueManager}")
	private String queueManager;

	@Value("${qm2.host}")
	private String host;

	@Value("${qm2.port}")
	private int port;

	@Value("${qm2.channel}")
	private String channel;

	@Value("${example.queue}")
	private String queue;

	@Bean
	public MQQueueConnectionFactory mqQueueConnectionFactory2() {
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
	public JmsTemplate jmsTemplate2() {
		JmsTemplate template = new JmsTemplate();
		template.setConnectionFactory(mqQueueConnectionFactory2());
		template.setPubSubDomain(false); // false for a Queue, true for a Topic
		template.setDefaultDestinationName(queue);;
		return template;
	}
	
	@Bean(name = "jmsMQContainerFactory2")
    public JmsListenerContainerFactory<?> JmsListenerContainerFactory() {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setPubSubDomain(false);
        factory.setConnectionFactory(mqQueueConnectionFactory2());
        return factory;
    }
}
