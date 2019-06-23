package cn.lanyj.snty.common.jedis;

import java.io.Serializable;

import redis.clients.jedis.params.SetParams;

public interface JedisContainer<T extends Serializable> {
	String mappedKey();

	SetParams getSetParams();

	void setSetParams(SetParams params);
	
	boolean isLoadPre();
	
	void setLoadPre(boolean loadPre);
	
	int size();
	
	void clear();
	
}
