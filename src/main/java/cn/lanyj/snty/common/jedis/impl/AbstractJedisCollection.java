package cn.lanyj.snty.common.jedis.impl;

import java.io.Serializable;

import cn.lanyj.snty.common.jedis.JedisContainer;
import cn.lanyj.snty.common.utils.JedisUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;

public abstract class AbstractJedisCollection<T extends Serializable> implements JedisContainer<T> {

	String mappedKey = null;
	boolean loadPre = false;
	SetParams params = null;

	public AbstractJedisCollection(String mappedKey) {
		this.mappedKey = mappedKey;
	}

	@Override
	public String mappedKey() {
		return mappedKey;
	}

	@Override
	public SetParams getSetParams() {
		return params;
	}

	@Override
	public void setSetParams(SetParams params) {
		this.params = params;
	}

	@Override
	public boolean isLoadPre() {
		return loadPre;
	}

	@Override
	public void setLoadPre(boolean loadPre) {
		this.loadPre = loadPre;
	}

	@Override
	public void clear() {
		Jedis jedis = JedisUtils.getJedis();
		jedis.del(mappedKey());
		jedis.close();
	}

}
