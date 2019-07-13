package cn.lanyj.snty.common.processor.handler;

import cn.lanyj.snty.common.msg.Message;
import cn.lanyj.snty.common.processor.ProcessorContext;
import cn.lanyj.snty.common.processor.ProcessorContext.State;
import cn.lanyj.snty.common.processor.ProcessorManager;
import cn.lanyj.snty.common.processor.impl.DefaultProcessorContext;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;

public class ProcessorHandler implements ChannelInboundHandler {

	private static final AttributeKey<ProcessorContext> CONTEXT = AttributeKey.valueOf("processor_context");

	ProcessorManager manager;

	public ProcessorHandler() {
	}

	public ProcessorHandler(ProcessorManager processorManager) {
		this.manager = processorManager;
	}

	public void setProcessorManager(ProcessorManager manager) {
		this.manager = manager;
	}

	public ProcessorManager getProcessorManager() {
		return manager;
	}

	private static ProcessorContext getOrCreateProcessorContext(ChannelHandlerContext context,
			ProcessorManager manager) {
		Attribute<ProcessorContext> attribute = context.channel().attr(CONTEXT);
		ProcessorContext processorContext = attribute.get();
		if (processorContext == null) {
			processorContext = new DefaultProcessorContext(context, manager);
			attribute.set(processorContext);
		}
		processorContext.setProcessorManager(manager);
		return processorContext;
	}

	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		getOrCreateProcessorContext(ctx, manager).handleChannelStateChanged(State.ADDED);
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		getOrCreateProcessorContext(ctx, manager).handleChannelStateChanged(State.REMOVED);
	}

	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		if (!getOrCreateProcessorContext(ctx, manager).handleChannelStateChanged(State.REGISTERED)) {
			ctx.fireChannelRegistered();
		}
	}

	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		if (!getOrCreateProcessorContext(ctx, manager).handleChannelStateChanged(State.UNREGISTERED)) {
			ctx.fireChannelUnregistered();
		}
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		if (!getOrCreateProcessorContext(ctx, manager).handleChannelStateChanged(State.ACTIVE)) {
			ctx.fireChannelActive();
		}
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		if (!getOrCreateProcessorContext(ctx, manager).handleChannelStateChanged(State.INACTIVE)) {
			ctx.fireChannelInactive();
		}
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if (msg instanceof Message) {
			if (getOrCreateProcessorContext(ctx, manager).handleMessage((Message<?>) msg)) {
				return;
			}
		}
		ctx.fireChannelRead(msg);
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.fireChannelReadComplete();
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if (!getOrCreateProcessorContext(ctx, manager).handleEvent(evt)) {
			ctx.fireUserEventTriggered(evt);
		}
	}

	@Override
	public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
		if (!getOrCreateProcessorContext(ctx, manager).handleChannelStateChanged(State.WRITABILITY_CHANGED)) {
			ctx.fireChannelWritabilityChanged();
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		if (!getOrCreateProcessorContext(ctx, manager).handleException(cause)) {
			ctx.fireExceptionCaught(cause);
		}
	}

}
