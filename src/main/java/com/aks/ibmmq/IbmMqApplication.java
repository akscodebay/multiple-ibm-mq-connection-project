package com.aks.ibmmq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;

@SpringBootApplication
public class IbmMqApplication implements CommandLineRunner {
	
	@Autowired
	private JmsTemplate jmsTemplate;
	
	@Autowired
	@Qualifier("jmsTemplate2")
	private JmsTemplate jmsTemplate2;

	public static void main(String[] args) {
		SpringApplication.run(IbmMqApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		int i=100;
		while(i>0) {
			jmsTemplate.convertAndSend("aks made it");
			jmsTemplate2.convertAndSend("aks made it twice");
			i--;
		}
		
		
	}
	
	@JmsListener(destination = "${example.queue}", containerFactory = "jmsMQContainerFactory")
    public void receive1(String text) {
        System.out.println("Received from qm1: " + text);
    }
	
	@JmsListener(destination = "${example.queue}", containerFactory = "jmsMQContainerFactory2")
    public void receive2(String text) {
        System.out.println("Received from qm2: " + text);
    }

}
