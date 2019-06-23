package cn.lanyj.snty.common.session;

import java.io.Serializable;
import java.util.Set;

import cn.lanyj.snty.common.utils.IdUtils;

public interface Session extends Serializable {

	String id();

	Set<String> keySet();

	void set(String key, Object value);

	<T> T get(String key);

	<T> T getOrDefault(String key, T defaultValue);

	boolean containsKey(String key);

	void clear();

	public static String nextId() {
		return IdUtils.nextUUIDAsString();
	}

	public static long currentTimeMillis() {
		return System.currentTimeMillis();
	}

}
