package cn.e3mall.Jedis;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
 

import cn.e3mall.common.jedis.JedisClient;

/**
 * @date 2017年7月12日
 * @author Administrator
 * @project e3-content-service
 */
public class JedisClientTest {

	@Test
	public void testJedisClient() {
		
		ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:/spring/applicationContext-redis.xml");
		JedisClient bean = applicationContext.getBean(JedisClient.class);
		bean.set("test", "123");
		String string = bean.get("test");
		System.out.println(string);
	}

}
