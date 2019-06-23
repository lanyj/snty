package cn.lanyj.snty.server.sessionhandler.msg;

import java.io.Serializable;

import cn.lanyj.snty.common.session.Session;

public class SessionMessageData implements Serializable {
	private static final long serialVersionUID = -2610357968515827227L;

	public static enum Type {
		CREATE, LOAD, DESTORY, GET, UPDATE, CLEAR
	}

	public Type type;
	public String sessionId;
	public Session session;
	
}
