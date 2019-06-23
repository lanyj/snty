package cn.lanyj.snty.common.jedis.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

import cn.lanyj.snty.common.jedis.JedisMap;
import cn.lanyj.snty.common.utils.DumpUtils;
import cn.lanyj.snty.common.utils.JedisUtils;
import redis.clients.jedis.Jedis;

public class DefaultJedisMap<T extends Serializable> extends AbstractJedisCollection<T> implements JedisMap<T> {

	public DefaultJedisMap(String mappedKey) {
		super(mappedKey);
	}

	@Override
	public int size() {
		Jedis jedis = JedisUtils.getJedis();
		long value = jedis.hlen(mappedKey);
		jedis.close();
		return (int) value;
	}

	@Override
	public boolean isEmpty() {
		return size() == 0;
	}

	@Override
	public boolean containsKey(Object key) {
		Jedis jedis = JedisUtils.getJedis();
		boolean val = jedis.hexists(mappedKey, String.valueOf(key));
		jedis.close();
		return val;
	}

	@Override
	public boolean containsValue(Object value) {
		String ec = DumpUtils.serializeAsUrlSafeString((Serializable) value);
		Jedis jedis = JedisUtils.getJedis();
		Map<String, String> map = jedis.hgetAll(mappedKey);
		jedis.close();
		return map.containsValue(ec);
	}

	@Override
	public T get(Object key) {
		Jedis jedis = JedisUtils.getJedis();
		String val = jedis.hget(mappedKey, String.valueOf(key));
		jedis.close();
		return DumpUtils.deserializeFromUrlSafeString(val);
	}

	@Override
	public T put(String key, T value) {
		if (isLoadPre()) {
			String ec = DumpUtils.serializeAsUrlSafeString(value);
			Jedis jedis = JedisUtils.getJedis();
			String pre = jedis.hget(mappedKey, key);
			jedis.hset(mappedKey, key, ec);
			jedis.close();

			return DumpUtils.deserializeFromUrlSafeString(pre);
		} else {
			String ec = DumpUtils.serializeAsUrlSafeString(value);
			Jedis jedis = JedisUtils.getJedis();
			jedis.hset(mappedKey, key, ec);
			jedis.close();

			return null;
		}
	}

	@Override
	public T remove(Object key) {
		if (isLoadPre()) {
			Jedis jedis = JedisUtils.getJedis();
			String pre = jedis.hget(mappedKey, String.valueOf(key));
			if (pre != null) {
				jedis.hdel(mappedKey, String.valueOf(key));
			}
			jedis.close();
			return DumpUtils.deserializeFromUrlSafeString(pre);
		} else {
			Jedis jedis = JedisUtils.getJedis();
			jedis.hdel(mappedKey, String.valueOf(key));
			jedis.close();
			return null;
		}
	}

	@Override
	public void putAll(Map<? extends String, ? extends T> m) {
		if (m.isEmpty()) {
			return;
		}
		Jedis jedis = JedisUtils.getJedis();
		m.forEach(new BiConsumer<String, T>() {
			@Override
			public void accept(String t, T u) {
				jedis.hset(mappedKey, t, DumpUtils.serializeAsUrlSafeString(u));
			}
		});
		jedis.close();
	}

	@Override
	public Set<String> keySet() {
		Jedis jedis = JedisUtils.getJedis();
		Set<String> keys = jedis.hkeys(mappedKey);
		jedis.close();
		return keys;
	}

	@Override
	public Collection<T> values() {
		Jedis jedis = JedisUtils.getJedis();
		List<String> vals = jedis.hvals(mappedKey);
		jedis.close();
		List<T> ret = new ArrayList<>();
		for (String v : vals) {
			ret.add(DumpUtils.deserializeFromUrlSafeString(v));
		}
		return ret;
	}

	@Override
	public Set<Entry<String, T>> entrySet() {
		Set<Entry<String, T>> set = new HashSet<>();

		Jedis jedis = JedisUtils.getJedis();
		Set<String> keys = jedis.hkeys(mappedKey);
		for (String key : keys) {
			String val = jedis.hget(mappedKey, key);
			set.add(new Entry<String, T>() {

				@Override
				public String getKey() {
					return key;
				}

				@Override
				public T getValue() {
					return DumpUtils.deserializeFromUrlSafeString(val);
				}

				@Override
				public T setValue(T value) {
					return put(key, value);
				}

			});
		}
		jedis.close();
		return set;
	}

}
