package cn.e3mall.activemq.test;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

/**
 * @date 2017年10月24日下午3:36:44
 * @Description: TODO
 * @authorAdministrator
 */
public class TestActivemqSpring {
	@Test
	public void sendMessage() {
	ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-activemq.xml");
	JmsTemplate jmsTemplate = applicationContext.getBean(JmsTemplate.class);
	Destination destination = (Destination)applicationContext.getBean("queueDestination");
	jmsTemplate.send(destination,new MessageCreator() {
		@Override
		public Message createMessage(Session session) throws JMSException {
			
			return session.createTextMessage("send Message");
		}
	});
	
	}

}
