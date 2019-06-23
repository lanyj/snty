package cn.lanyj.snty.common.processor.impl;

import cn.lanyj.snty.common.processor.ProcessorContext;
import cn.lanyj.snty.common.processor.ProcessorManager;
import cn.lanyj.snty.common.processor.exception.ProcessException;
import io.netty.channel.ChannelHandlerContext;

public class DefaultProcessorContext implements ProcessorContext {
	ChannelHandlerContext context;
	ProcessorManager manager;
	State state = State.NONE;

	public DefaultProcessorContext(ChannelHandlerContext context, ProcessorManager manager) {
		this.context = context;
		this.manager = manager;
	}

	@Override
	public ChannelHandlerContext getChannelHandlerContext() {
		return context;
	}

	@Override
	public ProcessorManager processorManager() {
		return manager;
	}

	@Override
	public void handleEvent(Object event) throws ProcessException {
		processorManager().handleEvent(this, event);
	}

	@Override
	public void handleException(Throwable t) throws ProcessException {
		processorManager().handleException(this, t);
	}

	@Override
	public void handleChannelStateChanged(State state) throws ProcessException {
		processorManager().handleChannelStateChanged(this, this.state, state);
		this.state = state;
	}

}
