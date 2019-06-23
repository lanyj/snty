package cn.lanyj.snty.common.session.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import cn.lanyj.snty.common.session.AbstractSession;
import cn.lanyj.snty.common.session.Session;

public class DefaultSyncSession extends AbstractSession {
	private static final long serialVersionUID = 2936419877873843368L;
	private final Map<String, Object> map = new HashMap<>();

	public DefaultSyncSession() {
		this(Session.nextId());
	}

	public DefaultSyncSession(String id) {
		super(id);
	}

	@Override
	public synchronized Set<String> keySet() {
		return map.keySet();
	}

	@Override
	public synchronized void set(String key, Object value) {
		map.put(key, value);
	}

	@SuppressWarnings("unchecked")
	@Override
	public synchronized <T> T get(String key) {
		return (T) map.get(key);
	}

	@SuppressWarnings("unchecked")
	@Override
	public synchronized <T> T getOrDefault(String key, T defaultValue) {
		Object ret = map.get(key);
		if (ret == null) {
			return defaultValue;
		}
		return (T) ret;
	}

	@Override
	public synchronized boolean containsKey(String key) {
		return map.containsKey(key);
	}

	@Override
	public synchronized void clear() {
		map.clear();
	}

}
