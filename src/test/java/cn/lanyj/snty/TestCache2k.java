package cn.lanyj.snty;

import java.util.concurrent.TimeUnit;

import org.cache2k.Cache;
import org.cache2k.Cache2kBuilder;

import cn.lanyj.snty.common.session.Session;
import cn.lanyj.snty.common.session.impl.DefaultSyncSession;
import junit.framework.Assert;
import junit.framework.TestCase;

public class TestCache2k extends TestCase {
	public TestCache2k(String name) {
		super(name);
	}

	public void testCache2k() {
		Cache<String, Session> cache = Cache2kBuilder.of(String.class, Session.class)
				.expireAfterWrite(30, TimeUnit.SECONDS).build();
		Session[] sessions = new Session[10];
		for (int i = 0; i < sessions.length; i++) {
			Session session = new DefaultSyncSession();
			sessions[i] = session;
			cache.put(session.id(), session);
		}

		Assert.assertNotNull(cache.get(sessions[0].id()));
	}
}
