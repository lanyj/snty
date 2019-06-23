package cn.lanyj.snty.common.utils;

import cn.lanyj.snty.common.config.GlobalConfig;
import cn.lanyj.snty.common.session.Session;
import cn.lanyj.snty.common.session.SessionManager;
import cn.lanyj.snty.common.session.TimeBasedSession;
import cn.lanyj.snty.common.session.impl.DefaultNonSyncSession;
import cn.lanyj.snty.common.session.impl.DefaultNonSyncTimeBasedSession;
import cn.lanyj.snty.common.session.impl.DefaultSyncSession;
import cn.lanyj.snty.common.session.impl.DefaultSyncTimeBasedSession;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;

public class SessionUtils {
	private static final GlobalConfig config = GlobalConfig.getInstance();
	public static final AttributeKey<Session> ATTR_SESSION = AttributeKey.valueOf("session");

	public static Session getSessionFromChannel(Channel channel) {
		Attribute<Session> attribute = channel.attr(ATTR_SESSION);
		Session session = attribute.get();
		return session;
	}

	public static void setSessionForChannel(Channel channel, Session session) {
		channel.attr(ATTR_SESSION).set(session);
	}

	public static void clearSessionForChannel(Channel channel) {
		channel.attr(ATTR_SESSION).set(null);
	}

	public static boolean hasSession(Channel channel) {
		return channel.hasAttr(ATTR_SESSION);
	}

	public static DefaultSyncSession newDefaultSyncSession() {
		return new DefaultSyncSession();
	}

	public static DefaultNonSyncSession newDefaultNonSyncSession() {
		return new DefaultNonSyncSession();
	}

	public static DefaultSyncTimeBasedSession newDefaultSyncTimeBasedSession() {
		return new DefaultSyncTimeBasedSession(config.sessionConfig.sessionExpiredTimeInMillis());
	}

	public static DefaultNonSyncTimeBasedSession newDefaultNonSyncTimeBasedSession() {
		return new DefaultNonSyncTimeBasedSession(config.sessionConfig.sessionExpiredTimeInMillis());
	}

	public static void updateOrRemoveSession(ChannelHandlerContext ctx, Session session) {
		if (session != null && (session instanceof TimeBasedSession)) {
			if (((TimeBasedSession) session).isValidate()) {
				((TimeBasedSession) session).update();
			} else {
				SessionUtils.clearSessionForChannel(ctx.channel());
			}
		}
	}

	public static void createOrUpdateSession(ChannelHandlerContext ctx) {
		Session session = SessionUtils.getSessionFromChannel(ctx.channel());
		if (session != null) {
			updateOrRemoveSession(ctx, session);
		} else {
			session = SessionUtils.newDefaultNonSyncTimeBasedSession();
			SessionUtils.setSessionForChannel(ctx.channel(), session);
		}
	}

	public static Session copySession(Session src, Session target) {
		if (src == null) {
			return target;
		}
		if (target == null) {
			return src;
		}
		for (String key : src.keySet()) {
			target.set(key, src.get(key));
		}
		return target;
	}

	public static void removeAndSaveSession(ChannelHandlerContext ctx) {
		Session session = SessionUtils.getSessionFromChannel(ctx.channel());
		if (session != null) {
			SessionUtils.clearSessionForChannel(ctx.channel());
			SessionManager.getInstance().saveSession(session);
		}
	}

}
