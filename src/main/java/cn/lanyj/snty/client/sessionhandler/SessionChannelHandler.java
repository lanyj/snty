package cn.lanyj.snty.client.sessionhandler;

import cn.lanyj.snty.common.msg.MessageStatus;
import cn.lanyj.snty.common.session.Session;
import cn.lanyj.snty.common.session.SessionManager;
import cn.lanyj.snty.common.session.TimeBasedSession;
import cn.lanyj.snty.common.utils.SessionUtils;
import cn.lanyj.snty.server.sessionhandler.msg.SessionMessage;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

@Sharable
public class SessionChannelHandler extends ChannelInboundHandlerAdapter {
	private static final SessionChannelHandler instance = new SessionChannelHandler();

	public static SessionChannelHandler getInstance() {
		return instance;
	}

	private SessionChannelHandler() {
	}

	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		super.handlerAdded(ctx);
		SessionUtils.createOrUpdateSession(ctx);
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		super.handlerRemoved(ctx);
		SessionUtils.removeAndSaveSession(ctx);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		Session session = SessionUtils.getSessionFromChannel(ctx.channel());
		SessionUtils.updateOrRemoveSession(ctx, session);

		if (msg instanceof SessionMessage) {
			SessionMessage sm = (SessionMessage) msg;
			switch (sm.content().type) {
			case LOAD: {
				String preSessionId = sm.content().sessionId;
				Session preSession = SessionManager.getInstance().loadSession(preSessionId);
				if (preSession == null) {
					sm.setStatus(MessageStatus.FAIL);
					ctx.writeAndFlush(sm);
				} else {
					if (preSession instanceof TimeBasedSession) {
						if (((TimeBasedSession) preSession).isValidate()) {
							((TimeBasedSession) preSession).update();

							preSession = SessionUtils.copySession(session, preSession);
						} else {
							preSession = null;
							sm.setStatus(MessageStatus.FAIL);
							ctx.writeAndFlush(sm);
						}
					}
					// update session ref as preSession
					session = preSession;
					SessionUtils.setSessionForChannel(ctx.channel(), session);
				}
				break;
			}
			case DESTORY: {
				SessionUtils.clearSessionForChannel(ctx.channel());
				sm.setStatus(MessageStatus.SUCCESS);
				ctx.writeAndFlush(sm);
				break;
			}
			case GET: {
				sm.content().session = session;
				sm.setStatus(MessageStatus.SUCCESS);
				ctx.writeAndFlush(sm);
				break;
			}
			case UPDATE: {
				Session ns = sm.content().session;
				session = SessionUtils.copySession(ns, session);
				sm.content().session = null;
				sm.setStatus(MessageStatus.SUCCESS);
				ctx.writeAndFlush(sm);
				break;
			}
			case CLEAR: {
				session.clear();
				sm.setStatus(MessageStatus.SUCCESS);
				ctx.writeAndFlush(sm);
				break;
			}
			case CREATE: {
				break;
			}
			default:
				break;
			}
		} else {
			super.channelRead(ctx, msg);
		}
	}

}
