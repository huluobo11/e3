package cn.e3mall.search.message;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * @date 2017年10月24日下午3:49:34
 * @Description: TODO
 * @authorAdministrator
 */
public class MyMessageListener implements MessageListener {

	@Override
	public void onMessage(Message message) {
		TextMessage textMessage = (TextMessage) message;
		String text;
		try {
			text=textMessage.getText();
			System.out.println(text);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

}
