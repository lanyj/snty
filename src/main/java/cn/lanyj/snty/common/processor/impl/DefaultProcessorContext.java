package cn.lanyj.snty.common.processor.impl;

import cn.lanyj.snty.common.processor.ProcessorContext;
import cn.lanyj.snty.common.processor.ProcessorManager;
import cn.lanyj.snty.common.processor.exception.ProcessException;
import io.netty.channel.ChannelHandlerContext;

public class DefaultProcessorContext implements ProcessorContext {
	ChannelHandlerContext context;
	ProcessorManager manager;
	State state = State.NONE;

	public DefaultProcessorContext(ChannelHandlerContext context) {
		this.context = context;
	}

	public DefaultProcessorContext(ChannelHandlerContext context, ProcessorManager manager) {
		this.context = context;
		this.manager = manager;
	}

	@Override
	public ChannelHandlerContext getChannelHandlerContext() {
		return context;
	}

	@Override
	public void setProcessorManager(ProcessorManager manager) {
		this.manager = manager;
	}

	@Override
	public ProcessorManager getProcessorManager() {
		return manager;
	}

	@Override
	public boolean handleEvent(Object event) throws ProcessException {
		return getProcessorManager().handleEvent(this, event);
	}

	@Override
	public boolean handleException(Throwable t) throws ProcessException {
		return getProcessorManager().handleException(this, t);
	}

	@Override
	public boolean handleChannelStateChanged(State state) throws ProcessException {
		boolean ret = getProcessorManager().handleChannelStateChanged(this, this.state, state);
		this.state = state;
		return ret;
	}

}
