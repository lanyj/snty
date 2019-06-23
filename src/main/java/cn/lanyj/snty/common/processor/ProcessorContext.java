package cn.lanyj.snty.common.processor;

import java.io.Serializable;

import cn.lanyj.snty.common.msg.Message;
import cn.lanyj.snty.common.processor.exception.ProcessException;
import cn.lanyj.snty.common.session.Session;
import cn.lanyj.snty.common.utils.SessionUtils;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;

public interface ProcessorContext {

	public static enum State {
		NONE, ADDED, REMOVED, REGISTERED, UNREGISTERED, ACTIVE, INACTIVE, WRITABILITY_CHANGED
	}

	ChannelHandlerContext getChannelHandlerContext();

	ProcessorManager processorManager();

	void handleEvent(Object event) throws ProcessException;

	void handleException(Throwable t) throws ProcessException;

	void handleChannelStateChanged(State state) throws ProcessException;

	default Session getContextSession() {
		return SessionUtils.getSessionFromChannel(getChannelHandlerContext().channel());
	}

	default <T extends Serializable> ChannelFuture writeMessage(Message<T> msg) {
		return getChannelHandlerContext().writeAndFlush(msg);
	}

	default <T extends Serializable> void handleMessage(Message<T> msg)
			throws ProcessException {
		processorManager().handleMessage(this, msg);
	}
}
