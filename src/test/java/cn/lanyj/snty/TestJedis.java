package cn.lanyj.snty;

import cn.lanyj.snty.common.utils.JedisUtils;
import cn.lanyj.snty.common.utils.UncaughtExceptionUtil;
import junit.framework.TestCase;
import redis.clients.jedis.Jedis;

public class TestJedis extends TestCase {

	public TestJedis(String name) {
		super(name);

		UncaughtExceptionUtil.declare();
	}

	public void testJedisConnect() {
		System.out.println(JedisUtils.getJedis().ping());

		for (int i = 0; i < 10; i++) {
			final int index = i;
			new Thread(new Runnable() {
				@Override
				public void run() {
					Jedis redis = JedisUtils.getJedis();
					redis.hset("key", "field", "Hello world!");
					System.out.println(index + ", " + redis.hget("key", "field"));
					redis.close();
				}
			}).start();
		}
	}

}
