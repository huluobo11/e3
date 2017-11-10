package cn.e3mall.Jedis;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;


/**
 * @date 2017年7月12日
 * @author Administrator
 * @project e3-content-service
 */
public class TestJdes {

	//测试单机版
	@Test
	public void testJedis() throws Exception{
		
		JedisPool pool = new JedisPool(new JedisPoolConfig(), "115.159.149.66", 6379, 6000); 

		Jedis jedis = pool.getResource(); 
		//Jedis jedis=new Jedis("115.159.149.66",6379);
		jedis.set("key11", "value11");
		String string = jedis.get("key11");
		System.out.println(string);
		jedis.close();
		pool.close();
	}
	//测试连接集群版
	@Test
	public void TestJedisCluster(){
		Set<HostAndPort>nodes=new HashSet<>();
		nodes.add(new HostAndPort("115.159.149.66",7001));
		nodes.add(new HostAndPort("115.159.149.66",7002));
		nodes.add(new HostAndPort("115.159.149.66",7003));
		nodes.add(new HostAndPort("115.159.149.66",7004));
		nodes.add(new HostAndPort("115.159.149.66",7005));
		nodes.add(new HostAndPort("115.159.149.66",7006));
		JedisCluster jedisCluster=new JedisCluster(nodes);
		jedisCluster.set("Kkey", "998");
		String string = jedisCluster.get("Kkey");
		System.out.println(string);
		jedisCluster.close();
	}

}
