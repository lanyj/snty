package cn.lanyj.snty.server.sessionhandler.msg;

import cn.lanyj.snty.common.msg.AbstractMessage;

public class SessionMessage extends AbstractMessage<SessionMessageData> {
	private static final long serialVersionUID = 732188137262512185L;

	public SessionMessage(String id, SessionMessageData content) {
		super(id, content);
	}

}
