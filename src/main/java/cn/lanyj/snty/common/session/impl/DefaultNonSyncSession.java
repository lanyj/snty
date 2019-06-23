package cn.lanyj.snty.common.session.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import cn.lanyj.snty.common.session.AbstractSession;
import cn.lanyj.snty.common.session.Session;

public class DefaultNonSyncSession extends AbstractSession implements Session {
	private static final long serialVersionUID = 1469111322016286705L;
	private final Map<String, Object> map = new HashMap<>();

	public DefaultNonSyncSession() {
		this(Session.nextId());
	}

	public DefaultNonSyncSession(String id) {
		super(id);
	}

	@Override
	public Set<String> keySet() {
		return map.keySet();
	}

	@Override
	public void set(String key, Object value) {
		map.put(key, value);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T get(String key) {
		return (T) map.get(key);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getOrDefault(String key, T defaultValue) {
		Object ret = map.get(key);
		if (ret == null) {
			return defaultValue;
		}
		return (T) ret;
	}

	@Override
	public boolean containsKey(String key) {
		return map.containsKey(key);
	}

	@Override
	public void clear() {
		map.clear();
	}

}
