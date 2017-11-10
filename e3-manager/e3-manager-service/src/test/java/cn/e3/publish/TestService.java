/**
 * 
 */
package cn.e3.publish;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @date 2017年7月11日
 * @author Administrator
 * @project e3-manager-service
 */
public class TestService {
	@Test
	public void testPublish() throws Exception {
		ApplicationContext application = new ClassPathXmlApplicationContext(
				"classpath:spring/applicationContext-*.xml");
		/*while (true) {
			Thread.sleep(1000);
		}*/
		System.out.println("服务已经启动。");
		System.in.read();
		System.out.println("服务已经关闭。");
	}
}