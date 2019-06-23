package cn.lanyj.snty.common.session.impl;

import cn.lanyj.snty.common.session.Session;
import cn.lanyj.snty.common.session.TimeBasedSession;

public class DefaultSyncTimeBasedSession extends DefaultSyncSession implements TimeBasedSession {
	private static final long serialVersionUID = 2768134653827525171L;
	private final long expiredTime;
	private long updateTime;

	public DefaultSyncTimeBasedSession(long expiredTimeMillis) {
		this(Session.nextId(), expiredTimeMillis);
	}

	public DefaultSyncTimeBasedSession(String id, long expiredTimeMillis) {
		super(id);

		this.expiredTime = expiredTimeMillis;
		this.updateTime = System.currentTimeMillis();
		update();
	}

	@Override
	public synchronized boolean isValidate() {
		return Session.currentTimeMillis() - this.updateTime <= this.expiredTime;
	}

	@Override
	public synchronized void update() {
		if (isValidate()) {
			this.updateTime = Session.currentTimeMillis();
		}
	}

}
