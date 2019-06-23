package cn.lanyj.snty.common.session;

import java.util.concurrent.TimeUnit;

import org.cache2k.Cache;
import org.cache2k.Cache2kBuilder;

import cn.lanyj.snty.common.config.GlobalConfig;

public class SessionManager {
	private static final GlobalConfig config = GlobalConfig.getInstance();

	Cache<String, Session> cache = Cache2kBuilder.of(String.class, Session.class)
			.expireAfterWrite(config.sessionConfig.sessionExpiredTimeInMillis(), TimeUnit.MILLISECONDS)
			.keepDataAfterExpired(false).boostConcurrency(true).disableStatistics(false).build();

	private static final SessionManager instance = new SessionManager();

	public static SessionManager getInstance() {
		return instance;
	}

	private SessionManager() {
	}

	public Session loadSession(String id) {
		return cache.get(id);
	}

	public void saveSession(Session session) {
		cache.put(session.id(), session);
	}

}
