package cn.lanyj.snty.common.utils;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.lanyj.snty.common.config.GlobalConfig;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class JedisUtils {
	private static final Logger logger = LoggerFactory.getLogger(JedisUtils.class);
	private static final GlobalConfig config = GlobalConfig.getInstance();

	private static final GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();

	private volatile static JedisPool pool;
	private volatile static boolean auth = false;
	private volatile static String password = "";

	static {
		init();
	}

	public static GenericObjectPoolConfig getGenericObjectPoolConfig() {
		poolConfig.setTestWhileIdle(true);
		poolConfig.setMaxTotal(16);
		return poolConfig;
	}

	private static void init() {
		pool = new JedisPool(poolConfig, config.jedisConfig.jedisHost(), config.jedisConfig.jedisPort(),
				config.jedisConfig.jedisTimeout());
		if (StringUtils.hasNonWhitespaceChar(config.jedisConfig.jedisPassword())) {
			auth = true;
			password = config.jedisConfig.jedisPassword();
		}
		logger.info("Jedis initialized with host = {}, port = {}, password = {}, timeout = {}",
				config.jedisConfig.jedisHost(), config.jedisConfig.jedisPort(), config.jedisConfig.jedisPassword(),
				config.jedisConfig.jedisTimeout());
	}

	public static Jedis getJedis() {
		Jedis jedis = pool.getResource();
		if (auth) {
			jedis.auth(password);
		}
		return jedis;
	}

	public static JedisPool getJedisPool() {
		return pool;
	}
}
