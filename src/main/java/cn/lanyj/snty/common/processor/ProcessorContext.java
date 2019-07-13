package cn.lanyj.snty.common.processor;

import java.io.Serializable;

import cn.lanyj.snty.common.msg.Message;
import cn.lanyj.snty.common.processor.exception.ProcessException;
import cn.lanyj.snty.common.session.Session;
import cn.lanyj.snty.common.utils.SessionUtils;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;

/**
 * processor context, holds native netty {@link ChannelHandlerContext} and
 * {@link ProcessorManager}, and used for trigger event method
 * 
 * @author lanyj
 *
 */
public interface ProcessorContext {

	public static enum State {
		NONE, ADDED, REMOVED, REGISTERED, UNREGISTERED, ACTIVE, INACTIVE, WRITABILITY_CHANGED
	}

	ChannelHandlerContext getChannelHandlerContext();

	ProcessorManager getProcessorManager();

	void setProcessorManager(ProcessorManager manager);

	boolean handleEvent(Object event) throws ProcessException;

	boolean handleException(Throwable t) throws ProcessException;

	boolean handleChannelStateChanged(State state) throws ProcessException;

	default Session getContextSession() {
		return SessionUtils.getSessionFromChannel(getChannelHandlerContext().channel());
	}

	default <T extends Serializable> ChannelFuture writeMessage(Message<T> msg) {
		return getChannelHandlerContext().writeAndFlush(msg);
	}

	default <T extends Serializable> boolean handleMessage(Message<T> msg) throws ProcessException {
		return getProcessorManager().handleMessage(this, msg);
	}
}
