package cn.lanyj.snty.common.session.impl;

import cn.lanyj.snty.common.session.Session;
import cn.lanyj.snty.common.session.TimeBasedSession;

public class DefaultNonSyncTimeBasedSession extends DefaultNonSyncSession implements TimeBasedSession {
	private static final long serialVersionUID = -4653667884106601851L;
	private final long expiredTime;
	private long updateTime;

	public DefaultNonSyncTimeBasedSession(long expiredTimeMillis) {
		this(Session.nextId(), expiredTimeMillis);
	}

	public DefaultNonSyncTimeBasedSession(String id, long expiredTimeMillis) {
		super(id);

		this.expiredTime = expiredTimeMillis;
		this.updateTime = System.currentTimeMillis();
		update();
	}

	@Override
	public boolean isValidate() {
		return Session.currentTimeMillis() - this.updateTime <= this.expiredTime;
	}

	@Override
	public void update() {
		if (isValidate()) {
			this.updateTime = Session.currentTimeMillis();
		}
	}

}
